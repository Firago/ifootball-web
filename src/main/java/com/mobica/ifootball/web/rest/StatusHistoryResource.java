package com.mobica.ifootball.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mobica.ifootball.domain.StatusHistory;
import com.mobica.ifootball.repository.StatusHistoryRepository;
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
 * REST controller for managing StatusHistory.
 */
@RestController
@RequestMapping("/api")
public class StatusHistoryResource {

    private final Logger log = LoggerFactory.getLogger(StatusHistoryResource.class);
        
    @Inject
    private StatusHistoryRepository statusHistoryRepository;
    
    /**
     * POST  /statusHistorys -> Create a new statusHistory.
     */
    @RequestMapping(value = "/statusHistorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StatusHistory> createStatusHistory(@Valid @RequestBody StatusHistory statusHistory) throws URISyntaxException {
        log.debug("REST request to save StatusHistory : {}", statusHistory);
        if (statusHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("statusHistory", "idexists", "A new statusHistory cannot already have an ID")).body(null);
        }
        StatusHistory result = statusHistoryRepository.save(statusHistory);
        return ResponseEntity.created(new URI("/api/statusHistorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("statusHistory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /statusHistorys -> Updates an existing statusHistory.
     */
    @RequestMapping(value = "/statusHistorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StatusHistory> updateStatusHistory(@Valid @RequestBody StatusHistory statusHistory) throws URISyntaxException {
        log.debug("REST request to update StatusHistory : {}", statusHistory);
        if (statusHistory.getId() == null) {
            return createStatusHistory(statusHistory);
        }
        StatusHistory result = statusHistoryRepository.save(statusHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("statusHistory", statusHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /statusHistorys -> get all the statusHistorys.
     */
    @RequestMapping(value = "/statusHistorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<StatusHistory> getAllStatusHistorys() {
        log.debug("REST request to get all StatusHistorys");
        return statusHistoryRepository.findAll();
            }

    /**
     * GET  /statusHistorys/:id -> get the "id" statusHistory.
     */
    @RequestMapping(value = "/statusHistorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StatusHistory> getStatusHistory(@PathVariable Long id) {
        log.debug("REST request to get StatusHistory : {}", id);
        StatusHistory statusHistory = statusHistoryRepository.findOne(id);
        return Optional.ofNullable(statusHistory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /statusHistorys/:id -> delete the "id" statusHistory.
     */
    @RequestMapping(value = "/statusHistorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStatusHistory(@PathVariable Long id) {
        log.debug("REST request to delete StatusHistory : {}", id);
        statusHistoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("statusHistory", id.toString())).build();
    }
}
