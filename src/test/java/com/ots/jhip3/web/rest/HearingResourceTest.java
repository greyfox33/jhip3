package com.ots.jhip3.web.rest;

import com.ots.jhip3.Application;
import com.ots.jhip3.domain.Hearing;
import com.ots.jhip3.repository.HearingRepository;

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
import org.springframework.util.Base64Utils;

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

import com.ots.jhip3.domain.enumeration.Statuscode;

/**
 * Test class for the HearingResource REST controller.
 *
 * @see HearingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HearingResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final Integer DEFAULT_COUNTY_CODE = 1;
    private static final Integer UPDATED_COUNTY_CODE = 2;
    private static final String DEFAULT_HEARING_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_HEARING_TYPE = "UPDATED_TEXT";
    private static final String DEFAULT_COURT_DEPT = "SAMPLE_TEXT";
    private static final String UPDATED_COURT_DEPT = "UPDATED_TEXT";

    private static final DateTime DEFAULT_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.print(DEFAULT_DATE);

    private static final Statuscode DEFAULT_STATUS = Statuscode.Created;
    private static final Statuscode UPDATED_STATUS = Statuscode.Sent;
    private static final String DEFAULT_CASEWORKER = "SAMPLE_TEXT";
    private static final String UPDATED_CASEWORKER = "UPDATED_TEXT";

    private static final Integer DEFAULT_CWCASEID = 1;
    private static final Integer UPDATED_CWCASEID = 2;

    private static final byte[] DEFAULT_DOC = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOC = TestUtil.createByteArray(2, "1");

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_SUMMARY = "SAMPLE_TEXT";
    private static final String UPDATED_SUMMARY = "UPDATED_TEXT";
    private static final String DEFAULT_ATTENDEE_FIRST = "SAMPLE_TEXT";
    private static final String UPDATED_ATTENDEE_FIRST = "UPDATED_TEXT";
    private static final String DEFAULT_ATTENDEE_LAST = "SAMPLE_TEXT";
    private static final String UPDATED_ATTENDEE_LAST = "UPDATED_TEXT";

    private static final DateTime DEFAULT_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_TIME_STR = dateTimeFormatter.print(DEFAULT_TIME);
    private static final String DEFAULT_LANGUAGE = "SAMPLE_TEXT";
    private static final String UPDATED_LANGUAGE = "UPDATED_TEXT";

    @Inject
    private HearingRepository hearingRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHearingMockMvc;

    private Hearing hearing;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HearingResource hearingResource = new HearingResource();
        ReflectionTestUtils.setField(hearingResource, "hearingRepository", hearingRepository);
        this.restHearingMockMvc = MockMvcBuilders.standaloneSetup(hearingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hearing = new Hearing();
        hearing.setCountyCode(DEFAULT_COUNTY_CODE);
        hearing.setHearingType(DEFAULT_HEARING_TYPE);
        hearing.setCourtDept(DEFAULT_COURT_DEPT);
        hearing.setDate(DEFAULT_DATE);
        hearing.setStatus(DEFAULT_STATUS);
        hearing.setCaseworker(DEFAULT_CASEWORKER);
        hearing.setCwcaseid(DEFAULT_CWCASEID);
        hearing.setDoc(DEFAULT_DOC);
        hearing.setImage(DEFAULT_IMAGE);
        hearing.setSummary(DEFAULT_SUMMARY);
        hearing.setAttendeeFirst(DEFAULT_ATTENDEE_FIRST);
        hearing.setAttendeeLast(DEFAULT_ATTENDEE_LAST);
        hearing.setTime(DEFAULT_TIME);
        hearing.setLanguage(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    public void createHearing() throws Exception {
        int databaseSizeBeforeCreate = hearingRepository.findAll().size();

        // Create the Hearing

        restHearingMockMvc.perform(post("/api/hearings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hearing)))
                .andExpect(status().isCreated());

        // Validate the Hearing in the database
        List<Hearing> hearings = hearingRepository.findAll();
        assertThat(hearings).hasSize(databaseSizeBeforeCreate + 1);
        Hearing testHearing = hearings.get(hearings.size() - 1);
        assertThat(testHearing.getCountyCode()).isEqualTo(DEFAULT_COUNTY_CODE);
        assertThat(testHearing.getHearingType()).isEqualTo(DEFAULT_HEARING_TYPE);
        assertThat(testHearing.getCourtDept()).isEqualTo(DEFAULT_COURT_DEPT);
        assertThat(testHearing.getDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DATE);
        assertThat(testHearing.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testHearing.getCaseworker()).isEqualTo(DEFAULT_CASEWORKER);
        assertThat(testHearing.getCwcaseid()).isEqualTo(DEFAULT_CWCASEID);
        assertThat(testHearing.getDoc()).isEqualTo(DEFAULT_DOC);
        assertThat(testHearing.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testHearing.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testHearing.getAttendeeFirst()).isEqualTo(DEFAULT_ATTENDEE_FIRST);
        assertThat(testHearing.getAttendeeLast()).isEqualTo(DEFAULT_ATTENDEE_LAST);
        assertThat(testHearing.getTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_TIME);
        assertThat(testHearing.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllHearings() throws Exception {
        // Initialize the database
        hearingRepository.saveAndFlush(hearing);

        // Get all the hearings
        restHearingMockMvc.perform(get("/api/hearings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hearing.getId().intValue())))
                .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)))
                .andExpect(jsonPath("$.[*].hearingType").value(hasItem(DEFAULT_HEARING_TYPE.toString())))
                .andExpect(jsonPath("$.[*].courtDept").value(hasItem(DEFAULT_COURT_DEPT.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].caseworker").value(hasItem(DEFAULT_CASEWORKER.toString())))
                .andExpect(jsonPath("$.[*].cwcaseid").value(hasItem(DEFAULT_CWCASEID)))
                .andExpect(jsonPath("$.[*].doc").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOC))))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
                .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
                .andExpect(jsonPath("$.[*].attendeeFirst").value(hasItem(DEFAULT_ATTENDEE_FIRST.toString())))
                .andExpect(jsonPath("$.[*].attendeeLast").value(hasItem(DEFAULT_ATTENDEE_LAST.toString())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)))
                .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }

    @Test
    @Transactional
    public void getHearing() throws Exception {
        // Initialize the database
        hearingRepository.saveAndFlush(hearing);

        // Get the hearing
        restHearingMockMvc.perform(get("/api/hearings/{id}", hearing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hearing.getId().intValue()))
            .andExpect(jsonPath("$.countyCode").value(DEFAULT_COUNTY_CODE))
            .andExpect(jsonPath("$.hearingType").value(DEFAULT_HEARING_TYPE.toString()))
            .andExpect(jsonPath("$.courtDept").value(DEFAULT_COURT_DEPT.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.caseworker").value(DEFAULT_CASEWORKER.toString()))
            .andExpect(jsonPath("$.cwcaseid").value(DEFAULT_CWCASEID))
            .andExpect(jsonPath("$.doc").value(Base64Utils.encodeToString(DEFAULT_DOC)))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY.toString()))
            .andExpect(jsonPath("$.attendeeFirst").value(DEFAULT_ATTENDEE_FIRST.toString()))
            .andExpect(jsonPath("$.attendeeLast").value(DEFAULT_ATTENDEE_LAST.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHearing() throws Exception {
        // Get the hearing
        restHearingMockMvc.perform(get("/api/hearings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHearing() throws Exception {
        // Initialize the database
        hearingRepository.saveAndFlush(hearing);

		int databaseSizeBeforeUpdate = hearingRepository.findAll().size();

        // Update the hearing
        hearing.setCountyCode(UPDATED_COUNTY_CODE);
        hearing.setHearingType(UPDATED_HEARING_TYPE);
        hearing.setCourtDept(UPDATED_COURT_DEPT);
        hearing.setDate(UPDATED_DATE);
        hearing.setStatus(UPDATED_STATUS);
        hearing.setCaseworker(UPDATED_CASEWORKER);
        hearing.setCwcaseid(UPDATED_CWCASEID);
        hearing.setDoc(UPDATED_DOC);
        hearing.setImage(UPDATED_IMAGE);
        hearing.setSummary(UPDATED_SUMMARY);
        hearing.setAttendeeFirst(UPDATED_ATTENDEE_FIRST);
        hearing.setAttendeeLast(UPDATED_ATTENDEE_LAST);
        hearing.setTime(UPDATED_TIME);
        hearing.setLanguage(UPDATED_LANGUAGE);
        

        restHearingMockMvc.perform(put("/api/hearings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hearing)))
                .andExpect(status().isOk());

        // Validate the Hearing in the database
        List<Hearing> hearings = hearingRepository.findAll();
        assertThat(hearings).hasSize(databaseSizeBeforeUpdate);
        Hearing testHearing = hearings.get(hearings.size() - 1);
        assertThat(testHearing.getCountyCode()).isEqualTo(UPDATED_COUNTY_CODE);
        assertThat(testHearing.getHearingType()).isEqualTo(UPDATED_HEARING_TYPE);
        assertThat(testHearing.getCourtDept()).isEqualTo(UPDATED_COURT_DEPT);
        assertThat(testHearing.getDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DATE);
        assertThat(testHearing.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testHearing.getCaseworker()).isEqualTo(UPDATED_CASEWORKER);
        assertThat(testHearing.getCwcaseid()).isEqualTo(UPDATED_CWCASEID);
        assertThat(testHearing.getDoc()).isEqualTo(UPDATED_DOC);
        assertThat(testHearing.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testHearing.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testHearing.getAttendeeFirst()).isEqualTo(UPDATED_ATTENDEE_FIRST);
        assertThat(testHearing.getAttendeeLast()).isEqualTo(UPDATED_ATTENDEE_LAST);
        assertThat(testHearing.getTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_TIME);
        assertThat(testHearing.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void deleteHearing() throws Exception {
        // Initialize the database
        hearingRepository.saveAndFlush(hearing);

		int databaseSizeBeforeDelete = hearingRepository.findAll().size();

        // Get the hearing
        restHearingMockMvc.perform(delete("/api/hearings/{id}", hearing.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Hearing> hearings = hearingRepository.findAll();
        assertThat(hearings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
