package com.iop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iop.IntegrationTest;
import com.iop.domain.Model;
import com.iop.repository.ModelRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ModelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CODE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ModelRepository modelRepository;

    @Mock
    private ModelRepository modelRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModelMockMvc;

    private Model model;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Model createEntity(EntityManager em) {
        Model model = new Model().name(DEFAULT_NAME).codeName(DEFAULT_CODE_NAME);
        return model;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Model createUpdatedEntity(EntityManager em) {
        Model model = new Model().name(UPDATED_NAME).codeName(UPDATED_CODE_NAME);
        return model;
    }

    @BeforeEach
    public void initTest() {
        model = createEntity(em);
    }

    @Test
    @Transactional
    void createModel() throws Exception {
        int databaseSizeBeforeCreate = modelRepository.findAll().size();
        // Create the Model
        restModelMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(model))
            )
            .andExpect(status().isCreated());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeCreate + 1);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModel.getCodeName()).isEqualTo(DEFAULT_CODE_NAME);
    }

    @Test
    @Transactional
    void createModelWithExistingId() throws Exception {
        // Create the Model with an existing ID
        model.setId(1L);

        int databaseSizeBeforeCreate = modelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModelMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(model))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllModels() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList
        restModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].codeName").value(hasItem(DEFAULT_CODE_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllModelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(modelRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restModelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(modelRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllModelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(modelRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restModelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(modelRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get the model
        restModelMockMvc
            .perform(get(ENTITY_API_URL_ID, model.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(model.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.codeName").value(DEFAULT_CODE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingModel() throws Exception {
        // Get the model
        restModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Update the model
        Model updatedModel = modelRepository.findById(model.getId()).get();
        // Disconnect from session so that the updates on updatedModel are not directly saved in db
        em.detach(updatedModel);
        updatedModel.name(UPDATED_NAME).codeName(UPDATED_CODE_NAME);

        restModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedModel.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedModel))
            )
            .andExpect(status().isOk());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModel.getCodeName()).isEqualTo(UPDATED_CODE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();
        model.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, model.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(model))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();
        model.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(model))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();
        model.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(model))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModelWithPatch() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Update the model using partial update
        Model partialUpdatedModel = new Model();
        partialUpdatedModel.setId(model.getId());

        partialUpdatedModel.codeName(UPDATED_CODE_NAME);

        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModel))
            )
            .andExpect(status().isOk());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModel.getCodeName()).isEqualTo(UPDATED_CODE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateModelWithPatch() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Update the model using partial update
        Model partialUpdatedModel = new Model();
        partialUpdatedModel.setId(model.getId());

        partialUpdatedModel.name(UPDATED_NAME).codeName(UPDATED_CODE_NAME);

        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModel))
            )
            .andExpect(status().isOk());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModel.getCodeName()).isEqualTo(UPDATED_CODE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();
        model.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, model.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(model))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();
        model.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(model))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();
        model.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(model))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        int databaseSizeBeforeDelete = modelRepository.findAll().size();

        // Delete the model
        restModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, model.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
