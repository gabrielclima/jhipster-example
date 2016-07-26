package br.com.tribosapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.tribosapp.domain.People;
import br.com.tribosapp.repository.PeopleRepository;
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
 * REST controller for managing People.
 */
@RestController
@RequestMapping("/api")
public class PeopleResource {

    private final Logger log = LoggerFactory.getLogger(PeopleResource.class);
        
    @Inject
    private PeopleRepository peopleRepository;
    
    /**
     * POST  /people : Create a new people.
     *
     * @param people the people to create
     * @return the ResponseEntity with status 201 (Created) and with body the new people, or with status 400 (Bad Request) if the people has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/people",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<People> createPeople(@RequestBody People people) throws URISyntaxException {
        log.debug("REST request to save People : {}", people);
        if (people.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("people", "idexists", "A new people cannot already have an ID")).body(null);
        }
        People result = peopleRepository.save(people);
        return ResponseEntity.created(new URI("/api/people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("people", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /people : Updates an existing people.
     *
     * @param people the people to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated people,
     * or with status 400 (Bad Request) if the people is not valid,
     * or with status 500 (Internal Server Error) if the people couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/people",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<People> updatePeople(@RequestBody People people) throws URISyntaxException {
        log.debug("REST request to update People : {}", people);
        if (people.getId() == null) {
            return createPeople(people);
        }
        People result = peopleRepository.save(people);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("people", people.getId().toString()))
            .body(result);
    }

    /**
     * GET  /people : get all the people.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of people in body
     */
    @RequestMapping(value = "/people",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<People> getAllPeople() {
        log.debug("REST request to get all People");
        List<People> people = peopleRepository.findAllWithEagerRelationships();
        return people;
    }

    /**
     * GET  /people/:id : get the "id" people.
     *
     * @param id the id of the people to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the people, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/people/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<People> getPeople(@PathVariable Long id) {
        log.debug("REST request to get People : {}", id);
        People people = peopleRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(people)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /people/:id : delete the "id" people.
     *
     * @param id the id of the people to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/people/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePeople(@PathVariable Long id) {
        log.debug("REST request to delete People : {}", id);
        peopleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("people", id.toString())).build();
    }

}
