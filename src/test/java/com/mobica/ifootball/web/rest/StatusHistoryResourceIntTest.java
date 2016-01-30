package com.mobica.ifootball.web.rest;

import com.mobica.ifootball.Application;
import com.mobica.ifootball.domain.StatusHistory;
import com.mobica.ifootball.repository.StatusHistoryRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mobica.ifootball.domain.enumeration.Status;

/**
 * Test class for the StatusHistoryResource REST controller.
 *
 * @see StatusHistoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StatusHistoryResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_STR = dateTimeFormatter.format(DEFAULT_TIME);
    
    private static final Status DEFAULT_STATUS = Status.free;
    private static final Status UPDATED_STATUS = Status.occupied;

    @Inject
    private StatusHistoryRepository statusHistoryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStatusHistoryMockMvc;

    private StatusHistory statusHistory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StatusHistoryResource statusHistoryResource = new StatusHistoryResource();
        ReflectionTestUtils.setField(statusHistoryResource, "statusHistoryRepository", statusHistoryRepository);
        this.restStatusHistoryMockMvc = MockMvcBuilders.standaloneSetup(statusHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        statusHistory = new StatusHistory();
        statusHistory.setTime(DEFAULT_TIME);
        statusHistory.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createStatusHistory() throws Exception {
        int databaseSizeBeforeCreate = statusHistoryRepository.findAll().size();

        // Create the StatusHistory

        restStatusHistoryMockMvc.perform(post("/api/statusHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(statusHistory)))
                .andExpect(status().isCreated());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistorys = statusHistoryRepository.findAll();
        assertThat(statusHistorys).hasSize(databaseSizeBeforeCreate + 1);
        StatusHistory testStatusHistory = statusHistorys.get(statusHistorys.size() - 1);
        assertThat(testStatusHistory.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testStatusHistory.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = statusHistoryRepository.findAll().size();
        // set the field null
        statusHistory.setTime(null);

        // Create the StatusHistory, which fails.

        restStatusHistoryMockMvc.perform(post("/api/statusHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(statusHistory)))
                .andExpect(status().isBadRequest());

        List<StatusHistory> statusHistorys = statusHistoryRepository.findAll();
        assertThat(statusHistorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = statusHistoryRepository.findAll().size();
        // set the field null
        statusHistory.setStatus(null);

        // Create the StatusHistory, which fails.

        restStatusHistoryMockMvc.perform(post("/api/statusHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(statusHistory)))
                .andExpect(status().isBadRequest());

        List<StatusHistory> statusHistorys = statusHistoryRepository.findAll();
        assertThat(statusHistorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStatusHistorys() throws Exception {
        // Initialize the database
        statusHistoryRepository.saveAndFlush(statusHistory);

        // Get all the statusHistorys
        restStatusHistoryMockMvc.perform(get("/api/statusHistorys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(statusHistory.getId().intValue())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getStatusHistory() throws Exception {
        // Initialize the database
        statusHistoryRepository.saveAndFlush(statusHistory);

        // Get the statusHistory
        restStatusHistoryMockMvc.perform(get("/api/statusHistorys/{id}", statusHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(statusHistory.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStatusHistory() throws Exception {
        // Get the statusHistory
        restStatusHistoryMockMvc.perform(get("/api/statusHistorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStatusHistory() throws Exception {
        // Initialize the database
        statusHistoryRepository.saveAndFlush(statusHistory);

		int databaseSizeBeforeUpdate = statusHistoryRepository.findAll().size();

        // Update the statusHistory
        statusHistory.setTime(UPDATED_TIME);
        statusHistory.setStatus(UPDATED_STATUS);

        restStatusHistoryMockMvc.perform(put("/api/statusHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(statusHistory)))
                .andExpect(status().isOk());

        // Validate the StatusHistory in the database
        List<StatusHistory> statusHistorys = statusHistoryRepository.findAll();
        assertThat(statusHistorys).hasSize(databaseSizeBeforeUpdate);
        StatusHistory testStatusHistory = statusHistorys.get(statusHistorys.size() - 1);
        assertThat(testStatusHistory.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testStatusHistory.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteStatusHistory() throws Exception {
        // Initialize the database
        statusHistoryRepository.saveAndFlush(statusHistory);

		int databaseSizeBeforeDelete = statusHistoryRepository.findAll().size();

        // Get the statusHistory
        restStatusHistoryMockMvc.perform(delete("/api/statusHistorys/{id}", statusHistory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StatusHistory> statusHistorys = statusHistoryRepository.findAll();
        assertThat(statusHistorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
