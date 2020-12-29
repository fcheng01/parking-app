package com.flashparking.parking.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.flashparking.parking.web.rest.TestUtil;

public class ParkingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parking.class);
        Parking parking1 = new Parking();
        parking1.setId(1L);
        Parking parking2 = new Parking();
        parking2.setId(parking1.getId());
        assertThat(parking1).isEqualTo(parking2);
        parking2.setId(2L);
        assertThat(parking1).isNotEqualTo(parking2);
        parking1.setId(null);
        assertThat(parking1).isNotEqualTo(parking2);
    }
}
