package com.ots.jhip3.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ots.jhip3.domain.Hearing;
import com.ots.jhip3.repository.HearingRepository;
import com.ots.jhip3.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Hearing.
 */
@RestController
@RequestMapping("/api")
public class HearingResource {

    private final Logger log = LoggerFactory.getLogger(HearingResource.class);

    @Inject
    private HearingRepository hearingRepository;

    /**
     * POST  /hearings -> Create a new hearing.
     */
    @RequestMapping(value = "/hearings",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hearing> createHearing(@RequestBody Hearing hearing) throws URISyntaxException {
        log.debug("REST request to save Hearing : {}", hearing);
        if (hearing.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hearing cannot already have an ID").body(null);
        }
        Hearing result = hearingRepository.save(hearing);
        return ResponseEntity.created(new URI("/api/hearings/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("hearing", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /hearings -> Updates an existing hearing.
     */
    @RequestMapping(value = "/hearings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hearing> updateHearing(@RequestBody Hearing hearing) throws URISyntaxException {
        log.debug("REST request to update Hearing : {}", hearing);
        if (hearing.getId() == null) {
            return createHearing(hearing);
        }
        Hearing result = hearingRepository.save(hearing);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("hearing", hearing.getId().toString()))
                .body(result);
    }

    /**
     * GET  /hearings -> get all the hearings.
     */
    @RequestMapping(value = "/hearings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Hearing> getAllHearings() {
        log.debug("REST request to get all Hearings");
        return hearingRepository.findAll();
    }

    /**
     * GET  /hearings/:id -> get the "id" hearing.
     */
    @RequestMapping(value = "/hearings/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hearing> getHearing(@PathVariable Long id) {
        log.debug("REST request to get Hearing : {}", id);
        return Optional.ofNullable(hearingRepository.findOne(id))
            .map(hearing -> new ResponseEntity<>(
                hearing,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /hearingsByCase/:id -> get all the hearings associated with a case.
     * (RMT-12/29/15) customized for case maintenance
     */
    @RequestMapping(value = "/hearingsbycase/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Hearing> getHearingsByCase(@PathVariable Long id) {
        log.debug("REST request to get HearingByCase : {}", id);
        return hearingRepository.findAllByCwcase(id);
    }
    
    /**
     * DELETE  /hearings/:id -> delete the "id" hearing.
     */
    @RequestMapping(value = "/hearings/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHearing(@PathVariable Long id) {
        log.debug("REST request to delete Hearing : {}", id);
        hearingRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hearing", id.toString())).build();
    }
}
