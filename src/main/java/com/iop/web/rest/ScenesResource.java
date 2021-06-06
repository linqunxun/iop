package com.iop.web.rest;

import com.iop.domain.Scenes;
import com.iop.repository.ScenesRepository;
import com.iop.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.iop.domain.Scenes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ScenesResource {

    private final Logger log = LoggerFactory.getLogger(ScenesResource.class);

    private static final String ENTITY_NAME = "scenes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScenesRepository scenesRepository;

    public ScenesResource(ScenesRepository scenesRepository) {
        this.scenesRepository = scenesRepository;
    }

    /**
     * {@code POST  /scenes} : Create a new scenes.
     *
     * @param scenes the scenes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scenes, or with status {@code 400 (Bad Request)} if the scenes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scenes")
    public ResponseEntity<Scenes> createScenes(@RequestBody Scenes scenes) throws URISyntaxException {
        log.debug("REST request to save Scenes : {}", scenes);
        if (scenes.getId() != null) {
            throw new BadRequestAlertException("A new scenes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Scenes result = scenesRepository.save(scenes);
        return ResponseEntity
            .created(new URI("/api/scenes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scenes/:id} : Updates an existing scenes.
     *
     * @param id the id of the scenes to save.
     * @param scenes the scenes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scenes,
     * or with status {@code 400 (Bad Request)} if the scenes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scenes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scenes/{id}")
    public ResponseEntity<Scenes> updateScenes(@PathVariable(value = "id", required = false) final Long id, @RequestBody Scenes scenes)
        throws URISyntaxException {
        log.debug("REST request to update Scenes : {}, {}", id, scenes);
        if (scenes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scenes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scenesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Scenes result = scenesRepository.save(scenes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scenes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /scenes/:id} : Partial updates given fields of an existing scenes, field will ignore if it is null
     *
     * @param id the id of the scenes to save.
     * @param scenes the scenes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scenes,
     * or with status {@code 400 (Bad Request)} if the scenes is not valid,
     * or with status {@code 404 (Not Found)} if the scenes is not found,
     * or with status {@code 500 (Internal Server Error)} if the scenes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/scenes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Scenes> partialUpdateScenes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Scenes scenes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Scenes partially : {}, {}", id, scenes);
        if (scenes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scenes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scenesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Scenes> result = scenesRepository
            .findById(scenes.getId())
            .map(
                existingScenes -> {
                    if (scenes.getName() != null) {
                        existingScenes.setName(scenes.getName());
                    }
                    if (scenes.getCover() != null) {
                        existingScenes.setCover(scenes.getCover());
                    }
                    if (scenes.getDesc() != null) {
                        existingScenes.setDesc(scenes.getDesc());
                    }
                    if (scenes.getDataSpec() != null) {
                        existingScenes.setDataSpec(scenes.getDataSpec());
                    }

                    return existingScenes;
                }
            )
            .map(scenesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scenes.getId().toString())
        );
    }

    /**
     * {@code GET  /scenes} : get all the scenes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scenes in body.
     */
    @GetMapping("/scenes")
    public ResponseEntity<List<Scenes>> getAllScenes(Pageable pageable) {
        log.debug("REST request to get a page of Scenes");
        Page<Scenes> page = scenesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scenes/:id} : get the "id" scenes.
     *
     * @param id the id of the scenes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scenes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scenes/{id}")
    public ResponseEntity<Scenes> getScenes(@PathVariable Long id) {
        log.debug("REST request to get Scenes : {}", id);
        Optional<Scenes> scenes = scenesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(scenes);
    }

    /**
     * {@code DELETE  /scenes/:id} : delete the "id" scenes.
     *
     * @param id the id of the scenes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scenes/{id}")
    public ResponseEntity<Void> deleteScenes(@PathVariable Long id) {
        log.debug("REST request to delete Scenes : {}", id);
        scenesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
