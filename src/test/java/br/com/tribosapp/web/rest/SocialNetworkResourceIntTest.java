package br.com.tribosapp.web.rest;

import br.com.tribosapp.TribosApp;
import br.com.tribosapp.domain.SocialNetwork;
import br.com.tribosapp.repository.SocialNetworkRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SocialNetworkResource REST controller.
 *
 * @see SocialNetworkResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TribosApp.class)
@WebAppConfiguration
@IntegrationTest
public class SocialNetworkResourceIntTest {


    private static final Long DEFAULT_SOCIAL_NETWORK_ID = 1L;
    private static final Long UPDATED_SOCIAL_NETWORK_ID = 2L;
    private static final String DEFAULT_SOCIAL_NETWORK_NAME = "AAAAA";
    private static final String UPDATED_SOCIAL_NETWORK_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_ICON = "AAAAA";
    private static final String UPDATED_ICON = "BBBBB";

    @Inject
    private SocialNetworkRepository socialNetworkRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSocialNetworkMockMvc;

    private SocialNetwork socialNetwork;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SocialNetworkResource socialNetworkResource = new SocialNetworkResource();
        ReflectionTestUtils.setField(socialNetworkResource, "socialNetworkRepository", socialNetworkRepository);
        this.restSocialNetworkMockMvc = MockMvcBuilders.standaloneSetup(socialNetworkResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        socialNetwork = new SocialNetwork();
        socialNetwork.setSocialNetworkId(DEFAULT_SOCIAL_NETWORK_ID);
        socialNetwork.setSocialNetworkName(DEFAULT_SOCIAL_NETWORK_NAME);
        socialNetwork.setDescription(DEFAULT_DESCRIPTION);
        socialNetwork.setIcon(DEFAULT_ICON);
    }

    @Test
    @Transactional
    public void createSocialNetwork() throws Exception {
        int databaseSizeBeforeCreate = socialNetworkRepository.findAll().size();

        // Create the SocialNetwork

        restSocialNetworkMockMvc.perform(post("/api/social-networks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socialNetwork)))
                .andExpect(status().isCreated());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworks = socialNetworkRepository.findAll();
        assertThat(socialNetworks).hasSize(databaseSizeBeforeCreate + 1);
        SocialNetwork testSocialNetwork = socialNetworks.get(socialNetworks.size() - 1);
        assertThat(testSocialNetwork.getSocialNetworkId()).isEqualTo(DEFAULT_SOCIAL_NETWORK_ID);
        assertThat(testSocialNetwork.getSocialNetworkName()).isEqualTo(DEFAULT_SOCIAL_NETWORK_NAME);
        assertThat(testSocialNetwork.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSocialNetwork.getIcon()).isEqualTo(DEFAULT_ICON);
    }

    @Test
    @Transactional
    public void getAllSocialNetworks() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);

        // Get all the socialNetworks
        restSocialNetworkMockMvc.perform(get("/api/social-networks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(socialNetwork.getId().intValue())))
                .andExpect(jsonPath("$.[*].socialNetworkId").value(hasItem(DEFAULT_SOCIAL_NETWORK_ID.intValue())))
                .andExpect(jsonPath("$.[*].socialNetworkName").value(hasItem(DEFAULT_SOCIAL_NETWORK_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())));
    }

    @Test
    @Transactional
    public void getSocialNetwork() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);

        // Get the socialNetwork
        restSocialNetworkMockMvc.perform(get("/api/social-networks/{id}", socialNetwork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(socialNetwork.getId().intValue()))
            .andExpect(jsonPath("$.socialNetworkId").value(DEFAULT_SOCIAL_NETWORK_ID.intValue()))
            .andExpect(jsonPath("$.socialNetworkName").value(DEFAULT_SOCIAL_NETWORK_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSocialNetwork() throws Exception {
        // Get the socialNetwork
        restSocialNetworkMockMvc.perform(get("/api/social-networks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSocialNetwork() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().size();

        // Update the socialNetwork
        SocialNetwork updatedSocialNetwork = new SocialNetwork();
        updatedSocialNetwork.setId(socialNetwork.getId());
        updatedSocialNetwork.setSocialNetworkId(UPDATED_SOCIAL_NETWORK_ID);
        updatedSocialNetwork.setSocialNetworkName(UPDATED_SOCIAL_NETWORK_NAME);
        updatedSocialNetwork.setDescription(UPDATED_DESCRIPTION);
        updatedSocialNetwork.setIcon(UPDATED_ICON);

        restSocialNetworkMockMvc.perform(put("/api/social-networks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSocialNetwork)))
                .andExpect(status().isOk());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworks = socialNetworkRepository.findAll();
        assertThat(socialNetworks).hasSize(databaseSizeBeforeUpdate);
        SocialNetwork testSocialNetwork = socialNetworks.get(socialNetworks.size() - 1);
        assertThat(testSocialNetwork.getSocialNetworkId()).isEqualTo(UPDATED_SOCIAL_NETWORK_ID);
        assertThat(testSocialNetwork.getSocialNetworkName()).isEqualTo(UPDATED_SOCIAL_NETWORK_NAME);
        assertThat(testSocialNetwork.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSocialNetwork.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    @Transactional
    public void deleteSocialNetwork() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);
        int databaseSizeBeforeDelete = socialNetworkRepository.findAll().size();

        // Get the socialNetwork
        restSocialNetworkMockMvc.perform(delete("/api/social-networks/{id}", socialNetwork.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SocialNetwork> socialNetworks = socialNetworkRepository.findAll();
        assertThat(socialNetworks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
