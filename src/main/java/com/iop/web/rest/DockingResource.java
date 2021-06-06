package com.iop.web.rest;

import com.iop.domain.Docking;
import com.iop.repository.DockingRepository;
import com.iop.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.iop.domain.Docking}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DockingResource {

    private final Logger log = LoggerFactory.getLogger(DockingResource.class);

    private static final String ENTITY_NAME = "docking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DockingRepository dockingRepository;

    public DockingResource(DockingRepository dockingRepository) {
        this.dockingRepository = dockingRepository;
    }

    /**
     * {@code POST  /dockings} : Create a new docking.
     *
     * @param docking the docking to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docking, or with status {@code 400 (Bad Request)} if the docking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dockings")
    public ResponseEntity<Docking> createDocking(@RequestBody Docking docking) throws URISyntaxException {
        log.debug("REST request to save Docking : {}", docking);
        if (docking.getId() != null) {
            throw new BadRequestAlertException("A new docking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Docking result = dockingRepository.save(docking);
        return ResponseEntity
            .created(new URI("/api/dockings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dockings/:id} : Updates an existing docking.
     *
     * @param id the id of the docking to save.
     * @param docking the docking to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docking,
     * or with status {@code 400 (Bad Request)} if the docking is not valid,
     * or with status {@code 500 (Internal Server Error)} if the docking couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dockings/{id}")
    public ResponseEntity<Docking> updateDocking(@PathVariable(value = "id", required = false) final Long id, @RequestBody Docking docking)
        throws URISyntaxException {
        log.debug("REST request to update Docking : {}, {}", id, docking);
        if (docking.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docking.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dockingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Docking result = dockingRepository.save(docking);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, docking.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dockings/:id} : Partial updates given fields of an existing docking, field will ignore if it is null
     *
     * @param id the id of the docking to save.
     * @param docking the docking to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docking,
     * or with status {@code 400 (Bad Request)} if the docking is not valid,
     * or with status {@code 404 (Not Found)} if the docking is not found,
     * or with status {@code 500 (Internal Server Error)} if the docking couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dockings/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Docking> partialUpdateDocking(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Docking docking
    ) throws URISyntaxException {
        log.debug("REST request to partial update Docking partially : {}, {}", id, docking);
        if (docking.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docking.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dockingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Docking> result = dockingRepository
            .findById(docking.getId())
            .map(
                existingDocking -> {
                    if (docking.getName() != null) {
                        existingDocking.setName(docking.getName());
                    }
                    if (docking.getVariable() != null) {
                        existingDocking.setVariable(docking.getVariable());
                    }

                    return existingDocking;
                }
            )
            .map(dockingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, docking.getId().toString())
        );
    }

    /**
     * {@code GET  /dockings} : get all the dockings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dockings in body.
     */
    @GetMapping("/dockings")
    public List<Docking> getAllDockings() {
        log.debug("REST request to get all Dockings");
        return dockingRepository.findAll();
    }

    /**
     * {@code GET  /dockings/:id} : get the "id" docking.
     *
     * @param id the id of the docking to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the docking, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dockings/{id}")
    public ResponseEntity<Docking> getDocking(@PathVariable Long id) {
        log.debug("REST request to get Docking : {}", id);
        Optional<Docking> docking = dockingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(docking);
    }

    /**
     * {@code DELETE  /dockings/:id} : delete the "id" docking.
     *
     * @param id the id of the docking to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dockings/{id}")
    public ResponseEntity<Void> deleteDocking(@PathVariable Long id) {
        log.debug("REST request to delete Docking : {}", id);
        dockingRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
