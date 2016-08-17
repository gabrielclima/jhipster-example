package br.com.tribosapp.web.rest;

import br.com.tribosapp.TribosApp;
import br.com.tribosapp.domain.Picture;
import br.com.tribosapp.repository.PictureRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.tribosapp.domain.enumeration.PictureType;

/**
 * Test class for the PictureResource REST controller.
 *
 * @see PictureResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = TribosApp.class)
//@WebAppConfiguration
//@IntegrationTest
public class PictureResourceIntTest {


    private static final String DEFAULT_PICTURE_TITLE = "AAAAA";
    private static final String UPDATED_PICTURE_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_FILE = "AAAAA";
    private static final String UPDATED_FILE = "BBBBB";

    private static final PictureType DEFAULT_TYPE = PictureType.ICON;
    private static final PictureType UPDATED_TYPE = PictureType.POST;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private PictureRepository pictureRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPictureMockMvc;

    private Picture picture;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PictureResource pictureResource = new PictureResource();
        ReflectionTestUtils.setField(pictureResource, "pictureRepository", pictureRepository);
        this.restPictureMockMvc = MockMvcBuilders.standaloneSetup(pictureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        picture = new Picture();
        picture.setPictureTitle(DEFAULT_PICTURE_TITLE);
        picture.setDescription(DEFAULT_DESCRIPTION);
        picture.setFile(DEFAULT_FILE);
        picture.setType(DEFAULT_TYPE);
        picture.setCreatedAt(DEFAULT_CREATED_AT);
        picture.setUpdatedAt(DEFAULT_UPDATED_AT);
    }

    //@Test
    @Transactional
    public void createPicture() throws Exception {
        int databaseSizeBeforeCreate = pictureRepository.findAll().size();

        // Create the Picture

        restPictureMockMvc.perform(post("/api/pictures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(picture)))
                .andExpect(status().isCreated());

        // Validate the Picture in the database
        List<Picture> pictures = pictureRepository.findAll();
        assertThat(pictures).hasSize(databaseSizeBeforeCreate + 1);
        Picture testPicture = pictures.get(pictures.size() - 1);
        assertThat(testPicture.getPictureTitle()).isEqualTo(DEFAULT_PICTURE_TITLE);
        assertThat(testPicture.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPicture.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testPicture.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPicture.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPicture.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    //@Test
    @Transactional
    public void getAllPictures() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        // Get all the pictures
        restPictureMockMvc.perform(get("/api/pictures?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(picture.getId().intValue())))
                .andExpect(jsonPath("$.[*].pictureTitle").value(hasItem(DEFAULT_PICTURE_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].file").value(hasItem(DEFAULT_FILE.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
                .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    //@Test
    @Transactional
    public void getPicture() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        // Get the picture
        restPictureMockMvc.perform(get("/api/pictures/{id}", picture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(picture.getId().intValue()))
            .andExpect(jsonPath("$.pictureTitle").value(DEFAULT_PICTURE_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.file").value(DEFAULT_FILE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    //@Test
    @Transactional
    public void getNonExistingPicture() throws Exception {
        // Get the picture
        restPictureMockMvc.perform(get("/api/pictures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    //@Test
    @Transactional
    public void updatePicture() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();

        // Update the picture
        Picture updatedPicture = new Picture();
        updatedPicture.setId(picture.getId());
        updatedPicture.setPictureTitle(UPDATED_PICTURE_TITLE);
        updatedPicture.setDescription(UPDATED_DESCRIPTION);
        updatedPicture.setFile(UPDATED_FILE);
        updatedPicture.setType(UPDATED_TYPE);
        updatedPicture.setCreatedAt(UPDATED_CREATED_AT);
        updatedPicture.setUpdatedAt(UPDATED_UPDATED_AT);

        restPictureMockMvc.perform(put("/api/pictures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPicture)))
                .andExpect(status().isOk());

        // Validate the Picture in the database
        List<Picture> pictures = pictureRepository.findAll();
        assertThat(pictures).hasSize(databaseSizeBeforeUpdate);
        Picture testPicture = pictures.get(pictures.size() - 1);
        assertThat(testPicture.getPictureTitle()).isEqualTo(UPDATED_PICTURE_TITLE);
        assertThat(testPicture.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPicture.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testPicture.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPicture.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPicture.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    //@Test
    @Transactional
    public void deletePicture() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);
        int databaseSizeBeforeDelete = pictureRepository.findAll().size();

        // Get the picture
        restPictureMockMvc.perform(delete("/api/pictures/{id}", picture.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Picture> pictures = pictureRepository.findAll();
        assertThat(pictures).hasSize(databaseSizeBeforeDelete - 1);
    }
}
