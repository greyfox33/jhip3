package com.ots.jhip3.web.rest;

import com.ots.jhip3.Application;
import com.ots.jhip3.domain.Child;
import com.ots.jhip3.repository.ChildRepository;

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
 * Test class for the ChildResource REST controller.
 *
 * @see ChildResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ChildResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_FIRST = "SAMPLE_TEXT";
    private static final String UPDATED_FIRST = "UPDATED_TEXT";
    private static final String DEFAULT_LAST = "SAMPLE_TEXT";
    private static final String UPDATED_LAST = "UPDATED_TEXT";

    private static final DateTime DEFAULT_DOB = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DOB = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DOB_STR = dateTimeFormatter.print(DEFAULT_DOB);
    private static final String DEFAULT_SSN = "SAMPLE_TEXT";
    private static final String UPDATED_SSN = "UPDATED_TEXT";

    private static final Integer DEFAULT_CASEFK = 1;
    private static final Integer UPDATED_CASEFK = 2;

    @Inject
    private ChildRepository childRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restChildMockMvc;

    private Child child;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChildResource childResource = new ChildResource();
        ReflectionTestUtils.setField(childResource, "childRepository", childRepository);
        this.restChildMockMvc = MockMvcBuilders.standaloneSetup(childResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        child = new Child();
        child.setFirst(DEFAULT_FIRST);
        child.setLast(DEFAULT_LAST);
        child.setDob(DEFAULT_DOB);
        child.setSsn(DEFAULT_SSN);
        child.setCasefk(DEFAULT_CASEFK);
    }

    @Test
    @Transactional
    public void createChild() throws Exception {
        int databaseSizeBeforeCreate = childRepository.findAll().size();

        // Create the Child

        restChildMockMvc.perform(post("/api/childs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(child)))
                .andExpect(status().isCreated());

        // Validate the Child in the database
        List<Child> childs = childRepository.findAll();
        assertThat(childs).hasSize(databaseSizeBeforeCreate + 1);
        Child testChild = childs.get(childs.size() - 1);
        assertThat(testChild.getFirst()).isEqualTo(DEFAULT_FIRST);
        assertThat(testChild.getLast()).isEqualTo(DEFAULT_LAST);
        assertThat(testChild.getDob().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DOB);
        assertThat(testChild.getSsn()).isEqualTo(DEFAULT_SSN);
        assertThat(testChild.getCasefk()).isEqualTo(DEFAULT_CASEFK);
    }

    @Test
    @Transactional
    public void getAllChilds() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get all the childs
        restChildMockMvc.perform(get("/api/childs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(child.getId().intValue())))
                .andExpect(jsonPath("$.[*].first").value(hasItem(DEFAULT_FIRST.toString())))
                .andExpect(jsonPath("$.[*].last").value(hasItem(DEFAULT_LAST.toString())))
                .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB_STR)))
                .andExpect(jsonPath("$.[*].ssn").value(hasItem(DEFAULT_SSN.toString())))
                .andExpect(jsonPath("$.[*].casefk").value(hasItem(DEFAULT_CASEFK)));
    }

    @Test
    @Transactional
    public void getChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get the child
        restChildMockMvc.perform(get("/api/childs/{id}", child.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(child.getId().intValue()))
            .andExpect(jsonPath("$.first").value(DEFAULT_FIRST.toString()))
            .andExpect(jsonPath("$.last").value(DEFAULT_LAST.toString()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB_STR))
            .andExpect(jsonPath("$.ssn").value(DEFAULT_SSN.toString()))
            .andExpect(jsonPath("$.casefk").value(DEFAULT_CASEFK));
    }

    @Test
    @Transactional
    public void getNonExistingChild() throws Exception {
        // Get the child
        restChildMockMvc.perform(get("/api/childs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

		int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child
        child.setFirst(UPDATED_FIRST);
        child.setLast(UPDATED_LAST);
        child.setDob(UPDATED_DOB);
        child.setSsn(UPDATED_SSN);
        child.setCasefk(UPDATED_CASEFK);
        

        restChildMockMvc.perform(put("/api/childs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(child)))
                .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childs = childRepository.findAll();
        assertThat(childs).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childs.get(childs.size() - 1);
        assertThat(testChild.getFirst()).isEqualTo(UPDATED_FIRST);
        assertThat(testChild.getLast()).isEqualTo(UPDATED_LAST);
        assertThat(testChild.getDob().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DOB);
        assertThat(testChild.getSsn()).isEqualTo(UPDATED_SSN);
        assertThat(testChild.getCasefk()).isEqualTo(UPDATED_CASEFK);
    }

    @Test
    @Transactional
    public void deleteChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

		int databaseSizeBeforeDelete = childRepository.findAll().size();

        // Get the child
        restChildMockMvc.perform(delete("/api/childs/{id}", child.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Child> childs = childRepository.findAll();
        assertThat(childs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
