package com.flashparking.parking.service;

import com.flashparking.parking.domain.Parking;
import com.flashparking.parking.repository.ParkingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Parking}.
 */
@Service
@Transactional
public class ParkingService {

    private final Logger log = LoggerFactory.getLogger(ParkingService.class);

    private final ParkingRepository parkingRepository;

    public ParkingService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    /**
     * Save a parking.
     *
     * @param parking the entity to save.
     * @return the persisted entity.
     */
    public Parking save(Parking parking) {
        log.debug("Request to save Parking : {}", parking);
        return parkingRepository.save(parking);
    }

    /**
     * Get all the parkings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Parking> findAll(Pageable pageable) {
        log.debug("Request to get all Parkings");
        return parkingRepository.findAll(pageable);
    }


    /**
     * Get one parking by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Parking> findOne(Long id) {
        log.debug("Request to get Parking : {}", id);
        return parkingRepository.findById(id);
    }

    /**
     * Delete the parking by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Parking : {}", id);
        parkingRepository.deleteById(id);
    }
}
