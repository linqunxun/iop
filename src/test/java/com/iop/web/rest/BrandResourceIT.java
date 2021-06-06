package com.iop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iop.IntegrationTest;
import com.iop.domain.Brand;
import com.iop.repository.BrandRepository;
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
 * Integration tests for the {@link BrandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BrandResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COVER = "AAAAAAAAAA";
    private static final String UPDATED_COVER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/brands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBrandMockMvc;

    private Brand brand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Brand createEntity(EntityManager em) {
        Brand brand = new Brand().name(DEFAULT_NAME).cover(DEFAULT_COVER);
        return brand;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Brand createUpdatedEntity(EntityManager em) {
        Brand brand = new Brand().name(UPDATED_NAME).cover(UPDATED_COVER);
        return brand;
    }

    @BeforeEach
    public void initTest() {
        brand = createEntity(em);
    }

    @Test
    @Transactional
    void createBrand() throws Exception {
        int databaseSizeBeforeCreate = brandRepository.findAll().size();
        // Create the Brand
        restBrandMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(brand))
            )
            .andExpect(status().isCreated());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeCreate + 1);
        Brand testBrand = brandList.get(brandList.size() - 1);
        assertThat(testBrand.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBrand.getCover()).isEqualTo(DEFAULT_COVER);
    }

    @Test
    @Transactional
    void createBrandWithExistingId() throws Exception {
        // Create the Brand with an existing ID
        brand.setId(1L);

        int databaseSizeBeforeCreate = brandRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrandMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(brand))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBrands() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList
        restBrandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brand.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cover").value(hasItem(DEFAULT_COVER)));
    }

    @Test
    @Transactional
    void getBrand() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get the brand
        restBrandMockMvc
            .perform(get(ENTITY_API_URL_ID, brand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(brand.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cover").value(DEFAULT_COVER));
    }

    @Test
    @Transactional
    void getNonExistingBrand() throws Exception {
        // Get the brand
        restBrandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBrand() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        int databaseSizeBeforeUpdate = brandRepository.findAll().size();

        // Update the brand
        Brand updatedBrand = brandRepository.findById(brand.getId()).get();
        // Disconnect from session so that the updates on updatedBrand are not directly saved in db
        em.detach(updatedBrand);
        updatedBrand.name(UPDATED_NAME).cover(UPDATED_COVER);

        restBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBrand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBrand))
            )
            .andExpect(status().isOk());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
        Brand testBrand = brandList.get(brandList.size() - 1);
        assertThat(testBrand.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBrand.getCover()).isEqualTo(UPDATED_COVER);
    }

    @Test
    @Transactional
    void putNonExistingBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().size();
        brand.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, brand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(brand))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().size();
        brand.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(brand))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().size();
        brand.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(brand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBrandWithPatch() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        int databaseSizeBeforeUpdate = brandRepository.findAll().size();

        // Update the brand using partial update
        Brand partialUpdatedBrand = new Brand();
        partialUpdatedBrand.setId(brand.getId());

        partialUpdatedBrand.name(UPDATED_NAME);

        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBrand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBrand))
            )
            .andExpect(status().isOk());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
        Brand testBrand = brandList.get(brandList.size() - 1);
        assertThat(testBrand.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBrand.getCover()).isEqualTo(DEFAULT_COVER);
    }

    @Test
    @Transactional
    void fullUpdateBrandWithPatch() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        int databaseSizeBeforeUpdate = brandRepository.findAll().size();

        // Update the brand using partial update
        Brand partialUpdatedBrand = new Brand();
        partialUpdatedBrand.setId(brand.getId());

        partialUpdatedBrand.name(UPDATED_NAME).cover(UPDATED_COVER);

        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBrand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBrand))
            )
            .andExpect(status().isOk());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
        Brand testBrand = brandList.get(brandList.size() - 1);
        assertThat(testBrand.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBrand.getCover()).isEqualTo(UPDATED_COVER);
    }

    @Test
    @Transactional
    void patchNonExistingBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().size();
        brand.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, brand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(brand))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().size();
        brand.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(brand))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().size();
        brand.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(brand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBrand() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        int databaseSizeBeforeDelete = brandRepository.findAll().size();

        // Delete the brand
        restBrandMockMvc
            .perform(delete(ENTITY_API_URL_ID, brand.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
