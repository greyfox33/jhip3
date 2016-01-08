package com.ots.jhip3.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ots.jhip3.domain.Child;
import com.ots.jhip3.repository.ChildRepository;
import com.ots.jhip3.web.rest.util.HeaderUtil;

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
 * REST controller for managing Child.
 */
@RestController
@RequestMapping("/api")
public class ChildResource {

    private final Logger log = LoggerFactory.getLogger(ChildResource.class);

    @Inject
    private ChildRepository childRepository;

    /**
     * POST  /childs -> Create a new child.
     */
    @RequestMapping(value = "/childs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Child> createChild(@Valid @RequestBody Child child) throws URISyntaxException {
        log.debug("REST request to save Child : {}", child);
        if (child.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new child cannot already have an ID").body(null);
        }
        Child result = childRepository.save(child);
        return ResponseEntity.created(new URI("/api/childs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("child", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /childs -> Updates an existing child.
     */
    @RequestMapping(value = "/childs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Child> updateChild(@Valid @RequestBody Child child) throws URISyntaxException {
        log.debug("REST request to update Child : {}", child);
        if (child.getId() == null) {
            return createChild(child);
        }
        Child result = childRepository.save(child);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("child", child.getId().toString()))
                .body(result);
    }

    /**
     * GET  /childs -> get all the childs.
     */
    @RequestMapping(value = "/childs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Child> getAllChilds() {
        log.debug("REST request to get all Childs");
        return childRepository.findAll();
    }

    /**
     * GET  /childs/:id -> get the "id" child.
     */
    @RequestMapping(value = "/childs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Child> getChild(@PathVariable Long id) {
        log.debug("REST request to get Child : {}", id);
        return Optional.ofNullable(childRepository.findOne(id))
            .map(child -> new ResponseEntity<>(
                child,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /childrenByCase/:id -> get all the children associated with a case.
     * (RMT-11/16/15) customized for case maintenance
     */
    @RequestMapping(value = "/childrenbycase/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Child> getChildrenByCase(@PathVariable Long id) {
        log.debug("REST request to get ChildrenByCase : {}", id);
        return childRepository.findAllByCwcase(id);
    }
    
    /**
     * DELETE  /childs/:id -> delete the "id" child.
     */
    @RequestMapping(value = "/childs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
        log.debug("REST request to delete Child : {}", id);
        childRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("child", id.toString())).build();
    }
}
