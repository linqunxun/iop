package com.iop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iop.IntegrationTest;
import com.iop.domain.Docking;
import com.iop.repository.DockingRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DockingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DockingResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VARIABLE = "AAAAAAAAAA";
    private static final String UPDATED_VARIABLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dockings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DockingRepository dockingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDockingMockMvc;

    private Docking docking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Docking createEntity(EntityManager em) {
        Docking docking = new Docking().name(DEFAULT_NAME).variable(DEFAULT_VARIABLE);
        return docking;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Docking createUpdatedEntity(EntityManager em) {
        Docking docking = new Docking().name(UPDATED_NAME).variable(UPDATED_VARIABLE);
        return docking;
    }

    @BeforeEach
    public void initTest() {
        docking = createEntity(em);
    }

    @Test
    @Transactional
    void createDocking() throws Exception {
        int databaseSizeBeforeCreate = dockingRepository.findAll().size();
        // Create the Docking
        restDockingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docking))
            )
            .andExpect(status().isCreated());

        // Validate the Docking in the database
        List<Docking> dockingList = dockingRepository.findAll();
        assertThat(dockingList).hasSize(databaseSizeBeforeCreate + 1);
        Docking testDocking = dockingList.get(dockingList.size() - 1);
        assertThat(testDocking.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocking.getVariable()).isEqualTo(DEFAULT_VARIABLE);
    }

    @Test
    @Transactional
    void createDockingWithExistingId() throws Exception {
        // Create the Docking with an existing ID
        docking.setId(1L);

        int databaseSizeBeforeCreate = dockingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDockingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Docking in the database
        List<Docking> dockingList = dockingRepository.findAll();
        assertThat(dockingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDockings() throws Exception {
        // Initialize the database
        dockingRepository.saveAndFlush(docking);

        // Get all the dockingList
        restDockingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docking.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].variable").value(hasItem(DEFAULT_VARIABLE)));
    }

    @Test
    @Transactional
    void getDocking() throws Exception {
        // Initialize the database
        dockingRepository.saveAndFlush(docking);

        // Get the docking
        restDockingMockMvc
            .perform(get(ENTITY_API_URL_ID, docking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docking.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.variable").value(DEFAULT_VARIABLE));
    }

    @Test
    @Transactional
    void getNonExistingDocking() throws Exception {
        // Get the docking
        restDockingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocking() throws Exception {
        // Initialize the database
        dockingRepository.saveAndFlush(docking);

        int databaseSizeBeforeUpdate = dockingRepository.findAll().size();

        // Update the docking
        Docking updatedDocking = dockingRepository.findById(docking.getId()).get();
        // Disconnect from session so that the updates on updatedDocking are not directly saved in db
        em.detach(updatedDocking);
        updatedDocking.name(UPDATED_NAME).variable(UPDATED_VARIABLE);

        restDockingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocking.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocking))
            )
            .andExpect(status().isOk());

        // Validate the Docking in the database
        List<Docking> dockingList = dockingRepository.findAll();
        assertThat(dockingList).hasSize(databaseSizeBeforeUpdate);
        Docking testDocking = dockingList.get(dockingList.size() - 1);
        assertThat(testDocking.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocking.getVariable()).isEqualTo(UPDATED_VARIABLE);
    }

    @Test
    @Transactional
    void putNonExistingDocking() throws Exception {
        int databaseSizeBeforeUpdate = dockingRepository.findAll().size();
        docking.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDockingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docking.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Docking in the database
        List<Docking> dockingList = dockingRepository.findAll();
        assertThat(dockingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocking() throws Exception {
        int databaseSizeBeforeUpdate = dockingRepository.findAll().size();
        docking.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDockingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Docking in the database
        List<Docking> dockingList = dockingRepository.findAll();
        assertThat(dockingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocking() throws Exception {
        int databaseSizeBeforeUpdate = dockingRepository.findAll().size();
        docking.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDockingMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docking))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Docking in the database
        List<Docking> dockingList = dockingRepository.findAll();
        assertThat(dockingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDockingWithPatch() throws Exception {
        // Initialize the database
        dockingRepository.saveAndFlush(docking);

        int databaseSizeBeforeUpdate = dockingRepository.findAll().size();

        // Update the docking using partial update
        Docking partialUpdatedDocking = new Docking();
        partialUpdatedDocking.setId(docking.getId());

        restDockingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocking.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocking))
            )
            .andExpect(status().isOk());

        // Validate the Docking in the database
        List<Docking> dockingList = dockingRepository.findAll();
        assertThat(dockingList).hasSize(databaseSizeBeforeUpdate);
        Docking testDocking = dockingList.get(dockingList.size() - 1);
        assertThat(testDocking.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocking.getVariable()).isEqualTo(DEFAULT_VARIABLE);
    }

    @Test
    @Transactional
    void fullUpdateDockingWithPatch() throws Exception {
        // Initialize the database
        dockingRepository.saveAndFlush(docking);

        int databaseSizeBeforeUpdate = dockingRepository.findAll().size();

        // Update the docking using partial update
        Docking partialUpdatedDocking = new Docking();
        partialUpdatedDocking.setId(docking.getId());

        partialUpdatedDocking.name(UPDATED_NAME).variable(UPDATED_VARIABLE);

        restDockingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocking.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocking))
            )
            .andExpect(status().isOk());

        // Validate the Docking in the database
        List<Docking> dockingList = dockingRepository.findAll();
        assertThat(dockingList).hasSize(databaseSizeBeforeUpdate);
        Docking testDocking = dockingList.get(dockingList.size() - 1);
        assertThat(testDocking.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocking.getVariable()).isEqualTo(UPDATED_VARIABLE);
    }

    @Test
    @Transactional
    void patchNonExistingDocking() throws Exception {
        int databaseSizeBeforeUpdate = dockingRepository.findAll().size();
        docking.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDockingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docking.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Docking in the database
        List<Docking> dockingList = dockingRepository.findAll();
        assertThat(dockingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocking() throws Exception {
        int databaseSizeBeforeUpdate = dockingRepository.findAll().size();
        docking.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDockingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Docking in the database
        List<Docking> dockingList = dockingRepository.findAll();
        assertThat(dockingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocking() throws Exception {
        int databaseSizeBeforeUpdate = dockingRepository.findAll().size();
        docking.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDockingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docking))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Docking in the database
        List<Docking> dockingList = dockingRepository.findAll();
        assertThat(dockingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocking() throws Exception {
        // Initialize the database
        dockingRepository.saveAndFlush(docking);

        int databaseSizeBeforeDelete = dockingRepository.findAll().size();

        // Delete the docking
        restDockingMockMvc
            .perform(delete(ENTITY_API_URL_ID, docking.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Docking> dockingList = dockingRepository.findAll();
        assertThat(dockingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
