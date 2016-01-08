package com.ots.jhip3.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ots.jhip3.domain.Cwcase;
import com.ots.jhip3.repository.CwcaseRepository;
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
 * REST controller for managing Cwcase.
 */
@RestController
@RequestMapping("/api")
public class CwcaseResource {

    private final Logger log = LoggerFactory.getLogger(CwcaseResource.class);

    @Inject
    private CwcaseRepository cwcaseRepository;

    /**
     * POST  /cwcases -> Create a new cwcase.
     */
    @RequestMapping(value = "/cwcases",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cwcase> createCwcase(@RequestBody Cwcase cwcase) throws URISyntaxException {
        log.debug("REST request to save Cwcase : {}", cwcase);
        if (cwcase.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new cwcase cannot already have an ID").body(null);
        }
        Cwcase result = cwcaseRepository.save(cwcase);
        return ResponseEntity.created(new URI("/api/cwcases/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("cwcase", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /cwcases -> Updates an existing cwcase.
     */
    @RequestMapping(value = "/cwcases",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cwcase> updateCwcase(@RequestBody Cwcase cwcase) throws URISyntaxException {
        log.debug("REST request to update Cwcase : {}", cwcase);
        if (cwcase.getId() == null) {
            return createCwcase(cwcase);
        }
        Cwcase result = cwcaseRepository.save(cwcase);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("cwcase", cwcase.getId().toString()))
                .body(result);
    }

    /**
     * GET  /cwcases -> get all the cwcases.
     */
    @RequestMapping(value = "/cwcases",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Cwcase> getAllCwcases() {
        log.debug("REST request to get all Cwcases");
        return cwcaseRepository.findAll();
    }

    /**
     * GET  /cwcases/:id -> get the "id" cwcase.
     */
    @RequestMapping(value = "/cwcases/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cwcase> getCwcase(@PathVariable Long id) {
        log.debug("REST request to get Cwcase : {}", id);
        return Optional.ofNullable(cwcaseRepository.findOne(id))
            .map(cwcase -> new ResponseEntity<>(
                cwcase,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cwcases/:id -> delete the "id" cwcase.
     */
    @RequestMapping(value = "/cwcases/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCwcase(@PathVariable Long id) {
        log.debug("REST request to delete Cwcase : {}", id);
        cwcaseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cwcase", id.toString())).build();
    }
}
