package br.com.tribosapp.web.rest;

import br.com.tribosapp.TribosApp;
import br.com.tribosapp.domain.Event;
import br.com.tribosapp.repository.EventRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EventResource REST controller.
 *
 * @see EventResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TribosApp.class)
@WebAppConfiguration
@IntegrationTest
public class EventResourceIntTest {


    private static final Long DEFAULT_EVENT_ID = 1L;
    private static final Long UPDATED_EVENT_ID = 2L;
    private static final String DEFAULT_EVENT_NAME = "AAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_PERIOD = "AAAAA";
    private static final String UPDATED_PERIOD = "BBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_TAGS = "AAAAA";
    private static final String UPDATED_TAGS = "BBBBB";
    private static final String DEFAULT_LINK = "AAAAA";
    private static final String UPDATED_LINK = "BBBBB";

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);
    private static final String DEFAULT_PICTURE = "AAAAA";
    private static final String UPDATED_PICTURE = "BBBBB";

    @Inject
    private EventRepository eventRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEventMockMvc;

    private Event event;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventResource eventResource = new EventResource();
        ReflectionTestUtils.setField(eventResource, "eventRepository", eventRepository);
        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        event = new Event();
        event.setEventId(DEFAULT_EVENT_ID);
        event.setEventName(DEFAULT_EVENT_NAME);
        event.setDescription(DEFAULT_DESCRIPTION);
        event.setPeriod(DEFAULT_PERIOD);
        event.setCreatedAt(DEFAULT_CREATED_AT);
        event.setTags(DEFAULT_TAGS);
        event.setLink(DEFAULT_LINK);
        event.setLatitude(DEFAULT_LATITUDE);
        event.setLongitude(DEFAULT_LONGITUDE);
        event.setPicture(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event

        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = events.get(events.size() - 1);
        assertThat(testEvent.getEventId()).isEqualTo(DEFAULT_EVENT_ID);
        assertThat(testEvent.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEvent.getPeriod()).isEqualTo(DEFAULT_PERIOD);
        assertThat(testEvent.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEvent.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testEvent.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testEvent.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testEvent.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testEvent.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the events
        restEventMockMvc.perform(get("/api/events?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
                .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID.intValue())))
                .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD.toString())))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
                .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())))
                .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
                .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE.toString())));
    }

    @Test
    @Transactional
    public void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.eventId").value(DEFAULT_EVENT_ID.intValue()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = new Event();
        updatedEvent.setId(event.getId());
        updatedEvent.setEventId(UPDATED_EVENT_ID);
        updatedEvent.setEventName(UPDATED_EVENT_NAME);
        updatedEvent.setDescription(UPDATED_DESCRIPTION);
        updatedEvent.setPeriod(UPDATED_PERIOD);
        updatedEvent.setCreatedAt(UPDATED_CREATED_AT);
        updatedEvent.setTags(UPDATED_TAGS);
        updatedEvent.setLink(UPDATED_LINK);
        updatedEvent.setLatitude(UPDATED_LATITUDE);
        updatedEvent.setLongitude(UPDATED_LONGITUDE);
        updatedEvent.setPicture(UPDATED_PICTURE);

        restEventMockMvc.perform(put("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEvent)))
                .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = events.get(events.size() - 1);
        assertThat(testEvent.getEventId()).isEqualTo(UPDATED_EVENT_ID);
        assertThat(testEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvent.getPeriod()).isEqualTo(UPDATED_PERIOD);
        assertThat(testEvent.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEvent.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testEvent.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testEvent.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testEvent.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testEvent.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    public void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Get the event
        restEventMockMvc.perform(delete("/api/events/{id}", event.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeDelete - 1);
    }
}
