package br.com.tribosapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.tribosapp.domain.SocialNetwork;
import br.com.tribosapp.repository.SocialNetworkRepository;
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
 * REST controller for managing SocialNetwork.
 */
@RestController
@RequestMapping("/api")
public class SocialNetworkResource {

    private final Logger log = LoggerFactory.getLogger(SocialNetworkResource.class);
        
    @Inject
    private SocialNetworkRepository socialNetworkRepository;
    
    /**
     * POST  /social-networks : Create a new socialNetwork.
     *
     * @param socialNetwork the socialNetwork to create
     * @return the ResponseEntity with status 201 (Created) and with body the new socialNetwork, or with status 400 (Bad Request) if the socialNetwork has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/social-networks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SocialNetwork> createSocialNetwork(@RequestBody SocialNetwork socialNetwork) throws URISyntaxException {
        log.debug("REST request to save SocialNetwork : {}", socialNetwork);
        if (socialNetwork.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("socialNetwork", "idexists", "A new socialNetwork cannot already have an ID")).body(null);
        }
        SocialNetwork result = socialNetworkRepository.save(socialNetwork);
        return ResponseEntity.created(new URI("/api/social-networks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("socialNetwork", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /social-networks : Updates an existing socialNetwork.
     *
     * @param socialNetwork the socialNetwork to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated socialNetwork,
     * or with status 400 (Bad Request) if the socialNetwork is not valid,
     * or with status 500 (Internal Server Error) if the socialNetwork couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/social-networks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SocialNetwork> updateSocialNetwork(@RequestBody SocialNetwork socialNetwork) throws URISyntaxException {
        log.debug("REST request to update SocialNetwork : {}", socialNetwork);
        if (socialNetwork.getId() == null) {
            return createSocialNetwork(socialNetwork);
        }
        SocialNetwork result = socialNetworkRepository.save(socialNetwork);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("socialNetwork", socialNetwork.getId().toString()))
            .body(result);
    }

    /**
     * GET  /social-networks : get all the socialNetworks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of socialNetworks in body
     */
    @RequestMapping(value = "/social-networks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SocialNetwork> getAllSocialNetworks() {
        log.debug("REST request to get all SocialNetworks");
        List<SocialNetwork> socialNetworks = socialNetworkRepository.findAll();
        return socialNetworks;
    }

    /**
     * GET  /social-networks/:id : get the "id" socialNetwork.
     *
     * @param id the id of the socialNetwork to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the socialNetwork, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/social-networks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SocialNetwork> getSocialNetwork(@PathVariable Long id) {
        log.debug("REST request to get SocialNetwork : {}", id);
        SocialNetwork socialNetwork = socialNetworkRepository.findOne(id);
        return Optional.ofNullable(socialNetwork)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /social-networks/:id : delete the "id" socialNetwork.
     *
     * @param id the id of the socialNetwork to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/social-networks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSocialNetwork(@PathVariable Long id) {
        log.debug("REST request to delete SocialNetwork : {}", id);
        socialNetworkRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("socialNetwork", id.toString())).build();
    }

}
