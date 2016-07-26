package br.com.tribosapp.web.rest;

import br.com.tribosapp.TribosApp;
import br.com.tribosapp.domain.Tribe;
import br.com.tribosapp.repository.TribeRepository;

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
 * Test class for the TribeResource REST controller.
 *
 * @see TribeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TribosApp.class)
@WebAppConfiguration
@IntegrationTest
public class TribeResourceIntTest {


    private static final Long DEFAULT_TRIBE_ID = 1L;
    private static final Long UPDATED_TRIBE_ID = 2L;
    private static final String DEFAULT_TRIBE_NAME = "AAAAA";
    private static final String UPDATED_TRIBE_NAME = "BBBBB";
    private static final String DEFAULT_TAGS = "AAAAA";
    private static final String UPDATED_TAGS = "BBBBB";

    @Inject
    private TribeRepository tribeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTribeMockMvc;

    private Tribe tribe;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TribeResource tribeResource = new TribeResource();
        ReflectionTestUtils.setField(tribeResource, "tribeRepository", tribeRepository);
        this.restTribeMockMvc = MockMvcBuilders.standaloneSetup(tribeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tribe = new Tribe();
        tribe.setTribeId(DEFAULT_TRIBE_ID);
        tribe.setTribeName(DEFAULT_TRIBE_NAME);
        tribe.setTags(DEFAULT_TAGS);
    }

    @Test
    @Transactional
    public void createTribe() throws Exception {
        int databaseSizeBeforeCreate = tribeRepository.findAll().size();

        // Create the Tribe

        restTribeMockMvc.perform(post("/api/tribes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tribe)))
                .andExpect(status().isCreated());

        // Validate the Tribe in the database
        List<Tribe> tribes = tribeRepository.findAll();
        assertThat(tribes).hasSize(databaseSizeBeforeCreate + 1);
        Tribe testTribe = tribes.get(tribes.size() - 1);
        assertThat(testTribe.getTribeId()).isEqualTo(DEFAULT_TRIBE_ID);
        assertThat(testTribe.getTribeName()).isEqualTo(DEFAULT_TRIBE_NAME);
        assertThat(testTribe.getTags()).isEqualTo(DEFAULT_TAGS);
    }

    @Test
    @Transactional
    public void getAllTribes() throws Exception {
        // Initialize the database
        tribeRepository.saveAndFlush(tribe);

        // Get all the tribes
        restTribeMockMvc.perform(get("/api/tribes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tribe.getId().intValue())))
                .andExpect(jsonPath("$.[*].tribeId").value(hasItem(DEFAULT_TRIBE_ID.intValue())))
                .andExpect(jsonPath("$.[*].tribeName").value(hasItem(DEFAULT_TRIBE_NAME.toString())))
                .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())));
    }

    @Test
    @Transactional
    public void getTribe() throws Exception {
        // Initialize the database
        tribeRepository.saveAndFlush(tribe);

        // Get the tribe
        restTribeMockMvc.perform(get("/api/tribes/{id}", tribe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tribe.getId().intValue()))
            .andExpect(jsonPath("$.tribeId").value(DEFAULT_TRIBE_ID.intValue()))
            .andExpect(jsonPath("$.tribeName").value(DEFAULT_TRIBE_NAME.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTribe() throws Exception {
        // Get the tribe
        restTribeMockMvc.perform(get("/api/tribes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTribe() throws Exception {
        // Initialize the database
        tribeRepository.saveAndFlush(tribe);
        int databaseSizeBeforeUpdate = tribeRepository.findAll().size();

        // Update the tribe
        Tribe updatedTribe = new Tribe();
        updatedTribe.setId(tribe.getId());
        updatedTribe.setTribeId(UPDATED_TRIBE_ID);
        updatedTribe.setTribeName(UPDATED_TRIBE_NAME);
        updatedTribe.setTags(UPDATED_TAGS);

        restTribeMockMvc.perform(put("/api/tribes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTribe)))
                .andExpect(status().isOk());

        // Validate the Tribe in the database
        List<Tribe> tribes = tribeRepository.findAll();
        assertThat(tribes).hasSize(databaseSizeBeforeUpdate);
        Tribe testTribe = tribes.get(tribes.size() - 1);
        assertThat(testTribe.getTribeId()).isEqualTo(UPDATED_TRIBE_ID);
        assertThat(testTribe.getTribeName()).isEqualTo(UPDATED_TRIBE_NAME);
        assertThat(testTribe.getTags()).isEqualTo(UPDATED_TAGS);
    }

    @Test
    @Transactional
    public void deleteTribe() throws Exception {
        // Initialize the database
        tribeRepository.saveAndFlush(tribe);
        int databaseSizeBeforeDelete = tribeRepository.findAll().size();

        // Get the tribe
        restTribeMockMvc.perform(delete("/api/tribes/{id}", tribe.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tribe> tribes = tribeRepository.findAll();
        assertThat(tribes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
