
package com.smarttraffic.intelligent;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdaptiveTrafficControllerTest {

    private final AdaptiveTrafficController controller =
            new AdaptiveTrafficController();

    @Test
    void testNormalTraffic() {
        TrafficSensor sensor = new TrafficSensor("NORD");
        sensor.updateVehicleCount(25);

        assertEquals(40,
            controller.calculateGreenDuration(sensor));
    }

    @Test
    void testMinimumGreenDuration() {
        TrafficSensor sensor = new TrafficSensor("EST");
        sensor.updateVehicleCount(0);

        assertEquals(15,
            controller.calculateGreenDuration(sensor));
    }

    @Test
    void testMaximumGreenDuration() {
        TrafficSensor sensor = new TrafficSensor("SUD");
        sensor.updateVehicleCount(100);

        assertEquals(45,
            controller.calculateGreenDuration(sensor));
    }

    @Test
    void testNegativeVehicleCount() {
        TrafficSensor sensor = new TrafficSensor("OUEST");

        assertThrows(IllegalArgumentException.class,
            () -> sensor.updateVehicleCount(-5));
    }

    @Test
    void testNullSensor() {
        assertThrows(IllegalArgumentException.class,
            () -> controller.calculateGreenDuration(null));
    }

    @Test
    void testCongestionLevel() {
        TrafficSensor sensor = new TrafficSensor("NORD");
        sensor.updateVehicleCount(25);

        assertEquals("FORTE",
            controller.analyzeTraffic(sensor, 30));
    }

    @Test
    void testEmergencyPriorityIntegration() {

        AdaptiveTrafficController controller =
            new AdaptiveTrafficController();

        assertFalse(controller.hasEmergency());

        controller.requestEmergencyPriority("EST");

        assertTrue(controller.hasEmergency());
        assertTrue(controller.shouldPrioritize("EST"));
        assertFalse(controller.shouldPrioritize("NORD"));
    }

    @Test
    void testReturnToNormalAfterEmergency() {

        AdaptiveTrafficController controller =
            new AdaptiveTrafficController();

        controller.requestEmergencyPriority("NORD");

        assertTrue(controller.shouldPrioritize("NORD"));

        controller.clearEmergencyPriority();

        assertFalse(controller.hasEmergency());
        assertFalse(controller.shouldPrioritize("NORD"));

        TrafficSensor sensor = new TrafficSensor("NORD");
        sensor.updateVehicleCount(25);

        assertEquals(40,
            controller.calculateGreenDuration(sensor));
    }

}
