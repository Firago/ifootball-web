package com.mobica.ifootball.web.rest;

import com.mobica.ifootball.Application;
import com.mobica.ifootball.domain.SensorData;
import com.mobica.ifootball.repository.SensorDataRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SensorDataResource REST controller.
 *
 * @see SensorDataResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SensorDataResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_STR = dateTimeFormatter.format(DEFAULT_TIME);

    private static final Float DEFAULT_VALUE = 0F;
    private static final Float UPDATED_VALUE = 1F;

    @Inject
    private SensorDataRepository sensorDataRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSensorDataMockMvc;

    private SensorData sensorData;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SensorDataResource sensorDataResource = new SensorDataResource();
        ReflectionTestUtils.setField(sensorDataResource, "sensorDataRepository", sensorDataRepository);
        this.restSensorDataMockMvc = MockMvcBuilders.standaloneSetup(sensorDataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sensorData = new SensorData();
        sensorData.setTime(DEFAULT_TIME);
        sensorData.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createSensorData() throws Exception {
        int databaseSizeBeforeCreate = sensorDataRepository.findAll().size();

        // Create the SensorData

        restSensorDataMockMvc.perform(post("/api/sensorData")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensorData)))
                .andExpect(status().isCreated());

        // Validate the SensorData in the database
        List<SensorData> sensorData = sensorDataRepository.findAll();
        assertThat(sensorData).hasSize(databaseSizeBeforeCreate + 1);
        SensorData testSensorData = sensorData.get(sensorData.size() - 1);
        assertThat(testSensorData.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testSensorData.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sensorDataRepository.findAll().size();
        // set the field null
        sensorData.setTime(null);

        // Create the SensorData, which fails.

        restSensorDataMockMvc.perform(post("/api/sensorData")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensorData)))
                .andExpect(status().isBadRequest());

        List<SensorData> sensorData = sensorDataRepository.findAll();
        assertThat(sensorData).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = sensorDataRepository.findAll().size();
        // set the field null
        sensorData.setValue(null);

        // Create the SensorData, which fails.

        restSensorDataMockMvc.perform(post("/api/sensorData")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensorData)))
                .andExpect(status().isBadRequest());

        List<SensorData> sensorData = sensorDataRepository.findAll();
        assertThat(sensorData).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSensorData() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorData
        restSensorDataMockMvc.perform(get("/api/sensorData?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sensorData.getId().intValue())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    public void getSensorData() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get the sensorData
        restSensorDataMockMvc.perform(get("/api/sensorData/{id}", sensorData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sensorData.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSensorData() throws Exception {
        // Get the sensorData
        restSensorDataMockMvc.perform(get("/api/sensorData/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensorData() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

		int databaseSizeBeforeUpdate = sensorDataRepository.findAll().size();

        // Update the sensorData
        sensorData.setTime(UPDATED_TIME);
        sensorData.setValue(UPDATED_VALUE);

        restSensorDataMockMvc.perform(put("/api/sensorData")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensorData)))
                .andExpect(status().isOk());

        // Validate the SensorData in the database
        List<SensorData> sensorData = sensorDataRepository.findAll();
        assertThat(sensorData).hasSize(databaseSizeBeforeUpdate);
        SensorData testSensorData = sensorData.get(sensorData.size() - 1);
        assertThat(testSensorData.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testSensorData.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteSensorData() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

		int databaseSizeBeforeDelete = sensorDataRepository.findAll().size();

        // Get the sensorData
        restSensorDataMockMvc.perform(delete("/api/sensorData/{id}", sensorData.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SensorData> sensorData = sensorDataRepository.findAll();
        assertThat(sensorData).hasSize(databaseSizeBeforeDelete - 1);
    }
}
