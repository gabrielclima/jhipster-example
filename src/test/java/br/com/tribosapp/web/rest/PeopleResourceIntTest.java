package br.com.tribosapp.web.rest;

import br.com.tribosapp.TribosApp;
import br.com.tribosapp.domain.People;
import br.com.tribosapp.repository.PeopleRepository;

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

import br.com.tribosapp.domain.enumeration.Gender;

/**
 * Test class for the PeopleResource REST controller.
 *
 * @see PeopleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TribosApp.class)
@WebAppConfiguration
@IntegrationTest
public class PeopleResourceIntTest {


    private static final Long DEFAULT_PEOPLE_ID = 1L;
    private static final Long UPDATED_PEOPLE_ID = 2L;

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;
    private static final String DEFAULT_PREFFERED_TRIBES = "AAAAA";
    private static final String UPDATED_PREFFERED_TRIBES = "BBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private PeopleRepository peopleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPeopleMockMvc;

    private People people;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PeopleResource peopleResource = new PeopleResource();
        ReflectionTestUtils.setField(peopleResource, "peopleRepository", peopleRepository);
        this.restPeopleMockMvc = MockMvcBuilders.standaloneSetup(peopleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        people = new People();
        people.setPeopleId(DEFAULT_PEOPLE_ID);
        people.setBirthDate(DEFAULT_BIRTH_DATE);
        people.setGender(DEFAULT_GENDER);
        people.setPrefferedTribes(DEFAULT_PREFFERED_TRIBES);
        people.setCreatedAt(DEFAULT_CREATED_AT);
        people.setUpdatedAt(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createPeople() throws Exception {
        int databaseSizeBeforeCreate = peopleRepository.findAll().size();

        // Create the People

        restPeopleMockMvc.perform(post("/api/people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(people)))
                .andExpect(status().isCreated());

        // Validate the People in the database
        List<People> people = peopleRepository.findAll();
        assertThat(people).hasSize(databaseSizeBeforeCreate + 1);
        People testPeople = people.get(people.size() - 1);
        assertThat(testPeople.getPeopleId()).isEqualTo(DEFAULT_PEOPLE_ID);
        assertThat(testPeople.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testPeople.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPeople.getPrefferedTribes()).isEqualTo(DEFAULT_PREFFERED_TRIBES);
        assertThat(testPeople.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPeople.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllPeople() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        // Get all the people
        restPeopleMockMvc.perform(get("/api/people?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(people.getId().intValue())))
                .andExpect(jsonPath("$.[*].peopleId").value(hasItem(DEFAULT_PEOPLE_ID.intValue())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
                .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
                .andExpect(jsonPath("$.[*].prefferedTribes").value(hasItem(DEFAULT_PREFFERED_TRIBES.toString())))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
                .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    public void getPeople() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        // Get the people
        restPeopleMockMvc.perform(get("/api/people/{id}", people.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(people.getId().intValue()))
            .andExpect(jsonPath("$.peopleId").value(DEFAULT_PEOPLE_ID.intValue()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.prefferedTribes").value(DEFAULT_PREFFERED_TRIBES.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPeople() throws Exception {
        // Get the people
        restPeopleMockMvc.perform(get("/api/people/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeople() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);
        int databaseSizeBeforeUpdate = peopleRepository.findAll().size();

        // Update the people
        People updatedPeople = new People();
        updatedPeople.setId(people.getId());
        updatedPeople.setPeopleId(UPDATED_PEOPLE_ID);
        updatedPeople.setBirthDate(UPDATED_BIRTH_DATE);
        updatedPeople.setGender(UPDATED_GENDER);
        updatedPeople.setPrefferedTribes(UPDATED_PREFFERED_TRIBES);
        updatedPeople.setCreatedAt(UPDATED_CREATED_AT);
        updatedPeople.setUpdatedAt(UPDATED_UPDATED_AT);

        restPeopleMockMvc.perform(put("/api/people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPeople)))
                .andExpect(status().isOk());

        // Validate the People in the database
        List<People> people = peopleRepository.findAll();
        assertThat(people).hasSize(databaseSizeBeforeUpdate);
        People testPeople = people.get(people.size() - 1);
        assertThat(testPeople.getPeopleId()).isEqualTo(UPDATED_PEOPLE_ID);
        assertThat(testPeople.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testPeople.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPeople.getPrefferedTribes()).isEqualTo(UPDATED_PREFFERED_TRIBES);
        assertThat(testPeople.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPeople.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void deletePeople() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);
        int databaseSizeBeforeDelete = peopleRepository.findAll().size();

        // Get the people
        restPeopleMockMvc.perform(delete("/api/people/{id}", people.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<People> people = peopleRepository.findAll();
        assertThat(people).hasSize(databaseSizeBeforeDelete - 1);
    }
}
