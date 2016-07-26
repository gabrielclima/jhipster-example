package br.com.tribosapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.tribosapp.domain.Tribe;
import br.com.tribosapp.repository.TribeRepository;
import br.com.tribosapp.web.rest.util.HeaderUtil;
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
 * REST controller for managing Tribe.
 */
@RestController
@RequestMapping("/api")
public class TribeResource {

    private final Logger log = LoggerFactory.getLogger(TribeResource.class);
        
    @Inject
    private TribeRepository tribeRepository;
    
    /**
     * POST  /tribes : Create a new tribe.
     *
     * @param tribe the tribe to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tribe, or with status 400 (Bad Request) if the tribe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tribes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tribe> createTribe(@RequestBody Tribe tribe) throws URISyntaxException {
        log.debug("REST request to save Tribe : {}", tribe);
        if (tribe.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tribe", "idexists", "A new tribe cannot already have an ID")).body(null);
        }
        Tribe result = tribeRepository.save(tribe);
        return ResponseEntity.created(new URI("/api/tribes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tribe", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tribes : Updates an existing tribe.
     *
     * @param tribe the tribe to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tribe,
     * or with status 400 (Bad Request) if the tribe is not valid,
     * or with status 500 (Internal Server Error) if the tribe couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tribes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tribe> updateTribe(@RequestBody Tribe tribe) throws URISyntaxException {
        log.debug("REST request to update Tribe : {}", tribe);
        if (tribe.getId() == null) {
            return createTribe(tribe);
        }
        Tribe result = tribeRepository.save(tribe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tribe", tribe.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tribes : get all the tribes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tribes in body
     */
    @RequestMapping(value = "/tribes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tribe> getAllTribes() {
        log.debug("REST request to get all Tribes");
        List<Tribe> tribes = tribeRepository.findAllWithEagerRelationships();
        return tribes;
    }

    /**
     * GET  /tribes/:id : get the "id" tribe.
     *
     * @param id the id of the tribe to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tribe, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tribes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tribe> getTribe(@PathVariable Long id) {
        log.debug("REST request to get Tribe : {}", id);
        Tribe tribe = tribeRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(tribe)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tribes/:id : delete the "id" tribe.
     *
     * @param id the id of the tribe to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tribes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTribe(@PathVariable Long id) {
        log.debug("REST request to delete Tribe : {}", id);
        tribeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tribe", id.toString())).build();
    }

}
