package com.mobica.ifootball.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mobica.ifootball.domain.SensorData;
import com.mobica.ifootball.repository.SensorDataRepository;
import com.mobica.ifootball.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SensorData.
 */
@RestController
@RequestMapping("/api")
public class SensorDataResource {

    private final Logger log = LoggerFactory.getLogger(SensorDataResource.class);
        
    @Inject
    private SensorDataRepository sensorDataRepository;
    
    /**
     * POST  /sensorDatas -> Create a new sensorData.
     */
    @RequestMapping(value = "/sensorDatas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SensorData> createSensorData(@Valid @RequestBody SensorData sensorData) throws URISyntaxException {
        log.debug("REST request to save SensorData : {}", sensorData);
        if (sensorData.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sensorData", "idexists", "A new sensorData cannot already have an ID")).body(null);
        }
        SensorData result = sensorDataRepository.save(sensorData);
        return ResponseEntity.created(new URI("/api/sensorDatas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sensorData", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sensorDatas -> Updates an existing sensorData.
     */
    @RequestMapping(value = "/sensorDatas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SensorData> updateSensorData(@Valid @RequestBody SensorData sensorData) throws URISyntaxException {
        log.debug("REST request to update SensorData : {}", sensorData);
        if (sensorData.getId() == null) {
            return createSensorData(sensorData);
        }
        SensorData result = sensorDataRepository.save(sensorData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sensorData", sensorData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sensorDatas -> get all the sensorDatas.
     */
    @RequestMapping(value = "/sensorDatas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SensorData> getAllSensorDatas() {
        log.debug("REST request to get all SensorDatas");
        return sensorDataRepository.findAll();
            }

    /**
     * GET  /sensorDatas/:id -> get the "id" sensorData.
     */
    @RequestMapping(value = "/sensorDatas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SensorData> getSensorData(@PathVariable Long id) {
        log.debug("REST request to get SensorData : {}", id);
        SensorData sensorData = sensorDataRepository.findOne(id);
        return Optional.ofNullable(sensorData)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sensorDatas/:id -> delete the "id" sensorData.
     */
    @RequestMapping(value = "/sensorDatas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSensorData(@PathVariable Long id) {
        log.debug("REST request to delete SensorData : {}", id);
        sensorDataRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sensorData", id.toString())).build();
    }
}
