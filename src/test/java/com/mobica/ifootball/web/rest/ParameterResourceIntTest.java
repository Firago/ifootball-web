package com.mobica.ifootball.web.rest;

import com.mobica.ifootball.Application;
import com.mobica.ifootball.domain.Parameter;
import com.mobica.ifootball.repository.ParameterRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mobica.ifootball.domain.enumeration.ParameterKey;
import com.mobica.ifootball.domain.enumeration.ParameterType;

/**
 * Test class for the ParameterResource REST controller.
 *
 * @see ParameterResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ParameterResourceIntTest {

    
    private static final ParameterKey DEFAULT_KEY = ParameterKey.VIBRATION_AMPLITUDE_THRESHOLD;
    private static final ParameterKey UPDATED_KEY = ParameterKey.VIBRATION_COUNT_THRESHOLD;
    
    private static final ParameterType DEFAULT_TYPE = ParameterType.STRING;
    private static final ParameterType UPDATED_TYPE = ParameterType.DECIMAL;
    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    @Inject
    private ParameterRepository parameterRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restParameterMockMvc;

    private Parameter parameter;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParameterResource parameterResource = new ParameterResource();
        ReflectionTestUtils.setField(parameterResource, "parameterRepository", parameterRepository);
        this.restParameterMockMvc = MockMvcBuilders.standaloneSetup(parameterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        parameter = new Parameter();
        parameter.setKey(DEFAULT_KEY);
        parameter.setType(DEFAULT_TYPE);
        parameter.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createParameter() throws Exception {
        int databaseSizeBeforeCreate = parameterRepository.findAll().size();

        // Create the Parameter

        restParameterMockMvc.perform(post("/api/parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parameter)))
                .andExpect(status().isCreated());

        // Validate the Parameter in the database
        List<Parameter> parameters = parameterRepository.findAll();
        assertThat(parameters).hasSize(databaseSizeBeforeCreate + 1);
        Parameter testParameter = parameters.get(parameters.size() - 1);
        assertThat(testParameter.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testParameter.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testParameter.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterRepository.findAll().size();
        // set the field null
        parameter.setKey(null);

        // Create the Parameter, which fails.

        restParameterMockMvc.perform(post("/api/parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parameter)))
                .andExpect(status().isBadRequest());

        List<Parameter> parameters = parameterRepository.findAll();
        assertThat(parameters).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterRepository.findAll().size();
        // set the field null
        parameter.setType(null);

        // Create the Parameter, which fails.

        restParameterMockMvc.perform(post("/api/parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parameter)))
                .andExpect(status().isBadRequest());

        List<Parameter> parameters = parameterRepository.findAll();
        assertThat(parameters).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParameters() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameters
        restParameterMockMvc.perform(get("/api/parameters?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
                .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get the parameter
        restParameterMockMvc.perform(get("/api/parameters/{id}", parameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(parameter.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParameter() throws Exception {
        // Get the parameter
        restParameterMockMvc.perform(get("/api/parameters/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

		int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

        // Update the parameter
        parameter.setKey(UPDATED_KEY);
        parameter.setType(UPDATED_TYPE);
        parameter.setValue(UPDATED_VALUE);

        restParameterMockMvc.perform(put("/api/parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parameter)))
                .andExpect(status().isOk());

        // Validate the Parameter in the database
        List<Parameter> parameters = parameterRepository.findAll();
        assertThat(parameters).hasSize(databaseSizeBeforeUpdate);
        Parameter testParameter = parameters.get(parameters.size() - 1);
        assertThat(testParameter.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testParameter.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testParameter.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

		int databaseSizeBeforeDelete = parameterRepository.findAll().size();

        // Get the parameter
        restParameterMockMvc.perform(delete("/api/parameters/{id}", parameter.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Parameter> parameters = parameterRepository.findAll();
        assertThat(parameters).hasSize(databaseSizeBeforeDelete - 1);
    }
}
