package com.ots.jhip3.web.rest;

import com.ots.jhip3.Application;
import com.ots.jhip3.domain.Cwcase;
import com.ots.jhip3.repository.CwcaseRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CwcaseResource REST controller.
 *
 * @see CwcaseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CwcaseResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_CREATEDATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATEDATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATEDATE_STR = dateTimeFormatter.print(DEFAULT_CREATEDATE);
    private static final String DEFAULT_CASESTATUS = "SAMPLE_TEXT";
    private static final String UPDATED_CASESTATUS = "UPDATED_TEXT";

    @Inject
    private CwcaseRepository cwcaseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCwcaseMockMvc;

    private Cwcase cwcase;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CwcaseResource cwcaseResource = new CwcaseResource();
        ReflectionTestUtils.setField(cwcaseResource, "cwcaseRepository", cwcaseRepository);
        this.restCwcaseMockMvc = MockMvcBuilders.standaloneSetup(cwcaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cwcase = new Cwcase();
        cwcase.setCreatedate(DEFAULT_CREATEDATE);
        cwcase.setCasestatus(DEFAULT_CASESTATUS);
    }

    @Test
    @Transactional
    public void createCwcase() throws Exception {
        int databaseSizeBeforeCreate = cwcaseRepository.findAll().size();

        // Create the Cwcase

        restCwcaseMockMvc.perform(post("/api/cwcases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cwcase)))
                .andExpect(status().isCreated());

        // Validate the Cwcase in the database
        List<Cwcase> cwcases = cwcaseRepository.findAll();
        assertThat(cwcases).hasSize(databaseSizeBeforeCreate + 1);
        Cwcase testCwcase = cwcases.get(cwcases.size() - 1);
        assertThat(testCwcase.getCreatedate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATEDATE);
        assertThat(testCwcase.getCasestatus()).isEqualTo(DEFAULT_CASESTATUS);
    }

    @Test
    @Transactional
    public void getAllCwcases() throws Exception {
        // Initialize the database
        cwcaseRepository.saveAndFlush(cwcase);

        // Get all the cwcases
        restCwcaseMockMvc.perform(get("/api/cwcases"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cwcase.getId().intValue())))
                .andExpect(jsonPath("$.[*].createdate").value(hasItem(DEFAULT_CREATEDATE_STR)))
                .andExpect(jsonPath("$.[*].casestatus").value(hasItem(DEFAULT_CASESTATUS.toString())));
    }

    @Test
    @Transactional
    public void getCwcase() throws Exception {
        // Initialize the database
        cwcaseRepository.saveAndFlush(cwcase);

        // Get the cwcase
        restCwcaseMockMvc.perform(get("/api/cwcases/{id}", cwcase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cwcase.getId().intValue()))
            .andExpect(jsonPath("$.createdate").value(DEFAULT_CREATEDATE_STR))
            .andExpect(jsonPath("$.casestatus").value(DEFAULT_CASESTATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCwcase() throws Exception {
        // Get the cwcase
        restCwcaseMockMvc.perform(get("/api/cwcases/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCwcase() throws Exception {
        // Initialize the database
        cwcaseRepository.saveAndFlush(cwcase);

		int databaseSizeBeforeUpdate = cwcaseRepository.findAll().size();

        // Update the cwcase
        cwcase.setCreatedate(UPDATED_CREATEDATE);
        cwcase.setCasestatus(UPDATED_CASESTATUS);
        

        restCwcaseMockMvc.perform(put("/api/cwcases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cwcase)))
                .andExpect(status().isOk());

        // Validate the Cwcase in the database
        List<Cwcase> cwcases = cwcaseRepository.findAll();
        assertThat(cwcases).hasSize(databaseSizeBeforeUpdate);
        Cwcase testCwcase = cwcases.get(cwcases.size() - 1);
        assertThat(testCwcase.getCreatedate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATEDATE);
        assertThat(testCwcase.getCasestatus()).isEqualTo(UPDATED_CASESTATUS);
    }

    @Test
    @Transactional
    public void deleteCwcase() throws Exception {
        // Initialize the database
        cwcaseRepository.saveAndFlush(cwcase);

		int databaseSizeBeforeDelete = cwcaseRepository.findAll().size();

        // Get the cwcase
        restCwcaseMockMvc.perform(delete("/api/cwcases/{id}", cwcase.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Cwcase> cwcases = cwcaseRepository.findAll();
        assertThat(cwcases).hasSize(databaseSizeBeforeDelete - 1);
    }
}
