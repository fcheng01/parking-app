package com.flashparking.parking.web.rest;

import com.flashparking.parking.ParkingApp;
import com.flashparking.parking.domain.Parking;
import com.flashparking.parking.repository.ParkingRepository;
import com.flashparking.parking.service.ParkingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ParkingResource} REST controller.
 */
@SpringBootTest(classes = ParkingApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ParkingResourceIT {

    private static final String DEFAULT_LOCATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL = 0;
    private static final Integer UPDATED_TOTAL = 1;

    private static final Integer DEFAULT_OCCUPIED_NUMBER = 0;
    private static final Integer UPDATED_OCCUPIED_NUMBER = 1;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParkingMockMvc;

    private Parking parking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parking createEntity(EntityManager em) {
        Parking parking = new Parking()
            .locationName(DEFAULT_LOCATION_NAME)
            .total(DEFAULT_TOTAL)
            .occupiedNumber(DEFAULT_OCCUPIED_NUMBER);
        return parking;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parking createUpdatedEntity(EntityManager em) {
        Parking parking = new Parking()
            .locationName(UPDATED_LOCATION_NAME)
            .total(UPDATED_TOTAL)
            .occupiedNumber(UPDATED_OCCUPIED_NUMBER);
        return parking;
    }

    @BeforeEach
    public void initTest() {
        parking = createEntity(em);
    }

    @Test
    @Transactional
    public void createParking() throws Exception {
        int databaseSizeBeforeCreate = parkingRepository.findAll().size();
        // Create the Parking
        restParkingMockMvc.perform(post("/api/parkings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parking)))
            .andExpect(status().isCreated());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeCreate + 1);
        Parking testParking = parkingList.get(parkingList.size() - 1);
        assertThat(testParking.getLocationName()).isEqualTo(DEFAULT_LOCATION_NAME);
        assertThat(testParking.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testParking.getOccupiedNumber()).isEqualTo(DEFAULT_OCCUPIED_NUMBER);
    }

    @Test
    @Transactional
    public void createParkingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parkingRepository.findAll().size();

        // Create the Parking with an existing ID
        parking.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParkingMockMvc.perform(post("/api/parkings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parking)))
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLocationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = parkingRepository.findAll().size();
        // set the field null
        parking.setLocationName(null);

        // Create the Parking, which fails.


        restParkingMockMvc.perform(post("/api/parkings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parking)))
            .andExpect(status().isBadRequest());

        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = parkingRepository.findAll().size();
        // set the field null
        parking.setTotal(null);

        // Create the Parking, which fails.


        restParkingMockMvc.perform(post("/api/parkings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parking)))
            .andExpect(status().isBadRequest());

        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOccupiedNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = parkingRepository.findAll().size();
        // set the field null
        parking.setOccupiedNumber(null);

        // Create the Parking, which fails.


        restParkingMockMvc.perform(post("/api/parkings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parking)))
            .andExpect(status().isBadRequest());

        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParkings() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList
        restParkingMockMvc.perform(get("/api/parkings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parking.getId().intValue())))
            .andExpect(jsonPath("$.[*].locationName").value(hasItem(DEFAULT_LOCATION_NAME)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].occupiedNumber").value(hasItem(DEFAULT_OCCUPIED_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getParking() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get the parking
        restParkingMockMvc.perform(get("/api/parkings/{id}", parking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parking.getId().intValue()))
            .andExpect(jsonPath("$.locationName").value(DEFAULT_LOCATION_NAME))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL))
            .andExpect(jsonPath("$.occupiedNumber").value(DEFAULT_OCCUPIED_NUMBER));
    }
    @Test
    @Transactional
    public void getNonExistingParking() throws Exception {
        // Get the parking
        restParkingMockMvc.perform(get("/api/parkings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParking() throws Exception {
        // Initialize the database
        parkingService.save(parking);

        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();

        // Update the parking
        Parking updatedParking = parkingRepository.findById(parking.getId()).get();
        // Disconnect from session so that the updates on updatedParking are not directly saved in db
        em.detach(updatedParking);
        updatedParking
            .locationName(UPDATED_LOCATION_NAME)
            .total(UPDATED_TOTAL)
            .occupiedNumber(UPDATED_OCCUPIED_NUMBER);

        restParkingMockMvc.perform(put("/api/parkings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedParking)))
            .andExpect(status().isOk());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
        Parking testParking = parkingList.get(parkingList.size() - 1);
        assertThat(testParking.getLocationName()).isEqualTo(UPDATED_LOCATION_NAME);
        assertThat(testParking.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testParking.getOccupiedNumber()).isEqualTo(UPDATED_OCCUPIED_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkingMockMvc.perform(put("/api/parkings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parking)))
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParking() throws Exception {
        // Initialize the database
        parkingService.save(parking);

        int databaseSizeBeforeDelete = parkingRepository.findAll().size();

        // Delete the parking
        restParkingMockMvc.perform(delete("/api/parkings/{id}", parking.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
