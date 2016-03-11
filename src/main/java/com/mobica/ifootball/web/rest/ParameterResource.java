package com.mobica.ifootball.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mobica.ifootball.domain.Parameter;
import com.mobica.ifootball.repository.ParameterRepository;
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
 * REST controller for managing Parameter.
 */
@RestController
@RequestMapping("/api")
public class ParameterResource {

    private final Logger log = LoggerFactory.getLogger(ParameterResource.class);
        
    @Inject
    private ParameterRepository parameterRepository;
    
    /**
     * POST  /parameters -> Create a new parameter.
     */
    @RequestMapping(value = "/parameters",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Parameter> createParameter(@Valid @RequestBody Parameter parameter) throws URISyntaxException {
        log.debug("REST request to save Parameter : {}", parameter);
        if (parameter.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("parameter", "idexists", "A new parameter cannot already have an ID")).body(null);
        }
        Parameter result = parameterRepository.save(parameter);
        return ResponseEntity.created(new URI("/api/parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("parameter", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /parameters -> Updates an existing parameter.
     */
    @RequestMapping(value = "/parameters",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Parameter> updateParameter(@Valid @RequestBody Parameter parameter) throws URISyntaxException {
        log.debug("REST request to update Parameter : {}", parameter);
        if (parameter.getId() == null) {
            return createParameter(parameter);
        }
        Parameter result = parameterRepository.save(parameter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("parameter", parameter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /parameters -> get all the parameters.
     */
    @RequestMapping(value = "/parameters",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Parameter> getAllParameters() {
        log.debug("REST request to get all Parameters");
        return parameterRepository.findAll();
            }

    /**
     * GET  /parameters/:id -> get the "id" parameter.
     */
    @RequestMapping(value = "/parameters/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Parameter> getParameter(@PathVariable Long id) {
        log.debug("REST request to get Parameter : {}", id);
        Parameter parameter = parameterRepository.findOne(id);
        return Optional.ofNullable(parameter)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /parameters/:id -> delete the "id" parameter.
     */
    @RequestMapping(value = "/parameters/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteParameter(@PathVariable Long id) {
        log.debug("REST request to delete Parameter : {}", id);
        parameterRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("parameter", id.toString())).build();
    }
}
