package com.flashparking.parking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Parking.
 */
@Entity
@Table(name = "parking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Parking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "location_name", nullable = false)
    private String locationName;

    @NotNull
    @Min(value = 0)
    @Column(name = "total", nullable = false)
    private Integer total;

    @NotNull
    @Min(value = 0)
    @Column(name = "occupied_number", nullable = false)
    private Integer occupiedNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public Parking locationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getTotal() {
        return total;
    }

    public Parking total(Integer total) {
        this.total = total;
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getOccupiedNumber() {
        return occupiedNumber;
    }

    public Parking occupiedNumber(Integer occupiedNumber) {
        this.occupiedNumber = occupiedNumber;
        return this;
    }

    public void setOccupiedNumber(Integer occupiedNumber) {
        this.occupiedNumber = occupiedNumber;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parking)) {
            return false;
        }
        return id != null && id.equals(((Parking) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parking{" +
            "id=" + getId() +
            ", locationName='" + getLocationName() + "'" +
            ", total=" + getTotal() +
            ", occupiedNumber=" + getOccupiedNumber() +
            "}";
    }
}
