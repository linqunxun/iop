package com.iop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iop.IntegrationTest;
import com.iop.domain.Scenes;
import com.iop.repository.ScenesRepository;
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
 * Integration tests for the {@link ScenesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScenesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COVER = "AAAAAAAAAA";
    private static final String UPDATED_COVER = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_SPEC = "AAAAAAAAAA";
    private static final String UPDATED_DATA_SPEC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/scenes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScenesRepository scenesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScenesMockMvc;

    private Scenes scenes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scenes createEntity(EntityManager em) {
        Scenes scenes = new Scenes().name(DEFAULT_NAME).cover(DEFAULT_COVER).desc(DEFAULT_DESC).dataSpec(DEFAULT_DATA_SPEC);
        return scenes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scenes createUpdatedEntity(EntityManager em) {
        Scenes scenes = new Scenes().name(UPDATED_NAME).cover(UPDATED_COVER).desc(UPDATED_DESC).dataSpec(UPDATED_DATA_SPEC);
        return scenes;
    }

    @BeforeEach
    public void initTest() {
        scenes = createEntity(em);
    }

    @Test
    @Transactional
    void createScenes() throws Exception {
        int databaseSizeBeforeCreate = scenesRepository.findAll().size();
        // Create the Scenes
        restScenesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scenes))
            )
            .andExpect(status().isCreated());

        // Validate the Scenes in the database
        List<Scenes> scenesList = scenesRepository.findAll();
        assertThat(scenesList).hasSize(databaseSizeBeforeCreate + 1);
        Scenes testScenes = scenesList.get(scenesList.size() - 1);
        assertThat(testScenes.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScenes.getCover()).isEqualTo(DEFAULT_COVER);
        assertThat(testScenes.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testScenes.getDataSpec()).isEqualTo(DEFAULT_DATA_SPEC);
    }

    @Test
    @Transactional
    void createScenesWithExistingId() throws Exception {
        // Create the Scenes with an existing ID
        scenes.setId(1L);

        int databaseSizeBeforeCreate = scenesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScenesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scenes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scenes in the database
        List<Scenes> scenesList = scenesRepository.findAll();
        assertThat(scenesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScenes() throws Exception {
        // Initialize the database
        scenesRepository.saveAndFlush(scenes);

        // Get all the scenesList
        restScenesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scenes.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cover").value(hasItem(DEFAULT_COVER)))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].dataSpec").value(hasItem(DEFAULT_DATA_SPEC)));
    }

    @Test
    @Transactional
    void getScenes() throws Exception {
        // Initialize the database
        scenesRepository.saveAndFlush(scenes);

        // Get the scenes
        restScenesMockMvc
            .perform(get(ENTITY_API_URL_ID, scenes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scenes.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cover").value(DEFAULT_COVER))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC))
            .andExpect(jsonPath("$.dataSpec").value(DEFAULT_DATA_SPEC));
    }

    @Test
    @Transactional
    void getNonExistingScenes() throws Exception {
        // Get the scenes
        restScenesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScenes() throws Exception {
        // Initialize the database
        scenesRepository.saveAndFlush(scenes);

        int databaseSizeBeforeUpdate = scenesRepository.findAll().size();

        // Update the scenes
        Scenes updatedScenes = scenesRepository.findById(scenes.getId()).get();
        // Disconnect from session so that the updates on updatedScenes are not directly saved in db
        em.detach(updatedScenes);
        updatedScenes.name(UPDATED_NAME).cover(UPDATED_COVER).desc(UPDATED_DESC).dataSpec(UPDATED_DATA_SPEC);

        restScenesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScenes.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScenes))
            )
            .andExpect(status().isOk());

        // Validate the Scenes in the database
        List<Scenes> scenesList = scenesRepository.findAll();
        assertThat(scenesList).hasSize(databaseSizeBeforeUpdate);
        Scenes testScenes = scenesList.get(scenesList.size() - 1);
        assertThat(testScenes.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScenes.getCover()).isEqualTo(UPDATED_COVER);
        assertThat(testScenes.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testScenes.getDataSpec()).isEqualTo(UPDATED_DATA_SPEC);
    }

    @Test
    @Transactional
    void putNonExistingScenes() throws Exception {
        int databaseSizeBeforeUpdate = scenesRepository.findAll().size();
        scenes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScenesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scenes.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scenes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scenes in the database
        List<Scenes> scenesList = scenesRepository.findAll();
        assertThat(scenesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScenes() throws Exception {
        int databaseSizeBeforeUpdate = scenesRepository.findAll().size();
        scenes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScenesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scenes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scenes in the database
        List<Scenes> scenesList = scenesRepository.findAll();
        assertThat(scenesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScenes() throws Exception {
        int databaseSizeBeforeUpdate = scenesRepository.findAll().size();
        scenes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScenesMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scenes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Scenes in the database
        List<Scenes> scenesList = scenesRepository.findAll();
        assertThat(scenesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScenesWithPatch() throws Exception {
        // Initialize the database
        scenesRepository.saveAndFlush(scenes);

        int databaseSizeBeforeUpdate = scenesRepository.findAll().size();

        // Update the scenes using partial update
        Scenes partialUpdatedScenes = new Scenes();
        partialUpdatedScenes.setId(scenes.getId());

        partialUpdatedScenes.name(UPDATED_NAME).desc(UPDATED_DESC);

        restScenesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScenes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScenes))
            )
            .andExpect(status().isOk());

        // Validate the Scenes in the database
        List<Scenes> scenesList = scenesRepository.findAll();
        assertThat(scenesList).hasSize(databaseSizeBeforeUpdate);
        Scenes testScenes = scenesList.get(scenesList.size() - 1);
        assertThat(testScenes.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScenes.getCover()).isEqualTo(DEFAULT_COVER);
        assertThat(testScenes.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testScenes.getDataSpec()).isEqualTo(DEFAULT_DATA_SPEC);
    }

    @Test
    @Transactional
    void fullUpdateScenesWithPatch() throws Exception {
        // Initialize the database
        scenesRepository.saveAndFlush(scenes);

        int databaseSizeBeforeUpdate = scenesRepository.findAll().size();

        // Update the scenes using partial update
        Scenes partialUpdatedScenes = new Scenes();
        partialUpdatedScenes.setId(scenes.getId());

        partialUpdatedScenes.name(UPDATED_NAME).cover(UPDATED_COVER).desc(UPDATED_DESC).dataSpec(UPDATED_DATA_SPEC);

        restScenesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScenes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScenes))
            )
            .andExpect(status().isOk());

        // Validate the Scenes in the database
        List<Scenes> scenesList = scenesRepository.findAll();
        assertThat(scenesList).hasSize(databaseSizeBeforeUpdate);
        Scenes testScenes = scenesList.get(scenesList.size() - 1);
        assertThat(testScenes.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScenes.getCover()).isEqualTo(UPDATED_COVER);
        assertThat(testScenes.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testScenes.getDataSpec()).isEqualTo(UPDATED_DATA_SPEC);
    }

    @Test
    @Transactional
    void patchNonExistingScenes() throws Exception {
        int databaseSizeBeforeUpdate = scenesRepository.findAll().size();
        scenes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScenesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scenes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scenes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scenes in the database
        List<Scenes> scenesList = scenesRepository.findAll();
        assertThat(scenesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScenes() throws Exception {
        int databaseSizeBeforeUpdate = scenesRepository.findAll().size();
        scenes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScenesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scenes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scenes in the database
        List<Scenes> scenesList = scenesRepository.findAll();
        assertThat(scenesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScenes() throws Exception {
        int databaseSizeBeforeUpdate = scenesRepository.findAll().size();
        scenes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScenesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scenes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Scenes in the database
        List<Scenes> scenesList = scenesRepository.findAll();
        assertThat(scenesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScenes() throws Exception {
        // Initialize the database
        scenesRepository.saveAndFlush(scenes);

        int databaseSizeBeforeDelete = scenesRepository.findAll().size();

        // Delete the scenes
        restScenesMockMvc
            .perform(delete(ENTITY_API_URL_ID, scenes.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Scenes> scenesList = scenesRepository.findAll();
        assertThat(scenesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
