package com.flashparking.parking.web.rest;

import com.flashparking.parking.domain.Parking;
import com.flashparking.parking.service.ParkingService;
import com.flashparking.parking.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.flashparking.parking.domain.Parking}.
 */
@RestController
@RequestMapping("/api")
public class ParkingResource {

    private final Logger log = LoggerFactory.getLogger(ParkingResource.class);

    private static final String ENTITY_NAME = "parking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParkingService parkingService;

    public ParkingResource(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    /**
     * {@code POST  /parkings} : Create a new parking.
     *
     * @param parking the parking to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parking, or with status {@code 400 (Bad Request)} if the parking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parkings")
    public ResponseEntity<Parking> createParking(@Valid @RequestBody Parking parking) throws URISyntaxException {
        log.debug("REST request to save Parking : {}", parking);
        if (parking.getId() != null) {
            throw new BadRequestAlertException("A new parking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Parking result = parkingService.save(parking);
        return ResponseEntity.created(new URI("/api/parkings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parkings} : Updates an existing parking.
     *
     * @param parking the parking to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parking,
     * or with status {@code 400 (Bad Request)} if the parking is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parking couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parkings")
    public ResponseEntity<Parking> updateParking(@Valid @RequestBody Parking parking) throws URISyntaxException {
        log.debug("REST request to update Parking : {}", parking);
        if (parking.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Parking result = parkingService.save(parking);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parking.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /parkings} : get all the parkings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parkings in body.
     */
    @GetMapping("/parkings")
    public ResponseEntity<List<Parking>> getAllParkings(Pageable pageable) {
        log.debug("REST request to get a page of Parkings");
        Page<Parking> page = parkingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parkings/:id} : get the "id" parking.
     *
     * @param id the id of the parking to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parking, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parkings/{id}")
    public ResponseEntity<Parking> getParking(@PathVariable Long id) {
        log.debug("REST request to get Parking : {}", id);
        Optional<Parking> parking = parkingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parking);
    }

    /**
     * {@code DELETE  /parkings/:id} : delete the "id" parking.
     *
     * @param id the id of the parking to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parkings/{id}")
    public ResponseEntity<Void> deleteParking(@PathVariable Long id) {
        log.debug("REST request to delete Parking : {}", id);
        parkingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
