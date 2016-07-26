package br.com.tribosapp.web.rest;

import br.com.tribosapp.TribosApp;
import br.com.tribosapp.domain.Setting;
import br.com.tribosapp.repository.SettingRepository;

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
 * Test class for the SettingResource REST controller.
 *
 * @see SettingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TribosApp.class)
@WebAppConfiguration
@IntegrationTest
public class SettingResourceIntTest {


    private static final Long DEFAULT_SETTING_ID = 1L;
    private static final Long UPDATED_SETTING_ID = 2L;
    private static final String DEFAULT_SETTING_KEY = "AAAAA";
    private static final String UPDATED_SETTING_KEY = "BBBBB";
    private static final String DEFAULT_SETTING_VALUE = "AAAAA";
    private static final String UPDATED_SETTING_VALUE = "BBBBB";

    @Inject
    private SettingRepository settingRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSettingMockMvc;

    private Setting setting;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SettingResource settingResource = new SettingResource();
        ReflectionTestUtils.setField(settingResource, "settingRepository", settingRepository);
        this.restSettingMockMvc = MockMvcBuilders.standaloneSetup(settingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        setting = new Setting();
        setting.setSettingId(DEFAULT_SETTING_ID);
        setting.setSettingKey(DEFAULT_SETTING_KEY);
        setting.setSettingValue(DEFAULT_SETTING_VALUE);
    }

    @Test
    @Transactional
    public void createSetting() throws Exception {
        int databaseSizeBeforeCreate = settingRepository.findAll().size();

        // Create the Setting

        restSettingMockMvc.perform(post("/api/settings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(setting)))
                .andExpect(status().isCreated());

        // Validate the Setting in the database
        List<Setting> settings = settingRepository.findAll();
        assertThat(settings).hasSize(databaseSizeBeforeCreate + 1);
        Setting testSetting = settings.get(settings.size() - 1);
        assertThat(testSetting.getSettingId()).isEqualTo(DEFAULT_SETTING_ID);
        assertThat(testSetting.getSettingKey()).isEqualTo(DEFAULT_SETTING_KEY);
        assertThat(testSetting.getSettingValue()).isEqualTo(DEFAULT_SETTING_VALUE);
    }

    @Test
    @Transactional
    public void getAllSettings() throws Exception {
        // Initialize the database
        settingRepository.saveAndFlush(setting);

        // Get all the settings
        restSettingMockMvc.perform(get("/api/settings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(setting.getId().intValue())))
                .andExpect(jsonPath("$.[*].settingId").value(hasItem(DEFAULT_SETTING_ID.intValue())))
                .andExpect(jsonPath("$.[*].settingKey").value(hasItem(DEFAULT_SETTING_KEY.toString())))
                .andExpect(jsonPath("$.[*].settingValue").value(hasItem(DEFAULT_SETTING_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getSetting() throws Exception {
        // Initialize the database
        settingRepository.saveAndFlush(setting);

        // Get the setting
        restSettingMockMvc.perform(get("/api/settings/{id}", setting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(setting.getId().intValue()))
            .andExpect(jsonPath("$.settingId").value(DEFAULT_SETTING_ID.intValue()))
            .andExpect(jsonPath("$.settingKey").value(DEFAULT_SETTING_KEY.toString()))
            .andExpect(jsonPath("$.settingValue").value(DEFAULT_SETTING_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSetting() throws Exception {
        // Get the setting
        restSettingMockMvc.perform(get("/api/settings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSetting() throws Exception {
        // Initialize the database
        settingRepository.saveAndFlush(setting);
        int databaseSizeBeforeUpdate = settingRepository.findAll().size();

        // Update the setting
        Setting updatedSetting = new Setting();
        updatedSetting.setId(setting.getId());
        updatedSetting.setSettingId(UPDATED_SETTING_ID);
        updatedSetting.setSettingKey(UPDATED_SETTING_KEY);
        updatedSetting.setSettingValue(UPDATED_SETTING_VALUE);

        restSettingMockMvc.perform(put("/api/settings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSetting)))
                .andExpect(status().isOk());

        // Validate the Setting in the database
        List<Setting> settings = settingRepository.findAll();
        assertThat(settings).hasSize(databaseSizeBeforeUpdate);
        Setting testSetting = settings.get(settings.size() - 1);
        assertThat(testSetting.getSettingId()).isEqualTo(UPDATED_SETTING_ID);
        assertThat(testSetting.getSettingKey()).isEqualTo(UPDATED_SETTING_KEY);
        assertThat(testSetting.getSettingValue()).isEqualTo(UPDATED_SETTING_VALUE);
    }

    @Test
    @Transactional
    public void deleteSetting() throws Exception {
        // Initialize the database
        settingRepository.saveAndFlush(setting);
        int databaseSizeBeforeDelete = settingRepository.findAll().size();

        // Get the setting
        restSettingMockMvc.perform(delete("/api/settings/{id}", setting.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Setting> settings = settingRepository.findAll();
        assertThat(settings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
