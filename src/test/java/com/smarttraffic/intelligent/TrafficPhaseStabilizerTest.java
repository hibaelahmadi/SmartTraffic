
package com.smarttraffic.intelligent;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrafficPhaseStabilizerTest {

    @Test
    void testInitialPhase() {
        TrafficPhaseStabilizer stabilizer =
            new TrafficPhaseStabilizer("NORTH_SOUTH");

        assertEquals("NORTH_SOUTH",
            stabilizer.getCurrentPhase());

        assertEquals(0,
            stabilizer.getElapsedGreenTime());
    }

    @Test
    void testNoSwitchBeforeMinimumDuration() {
        TrafficPhaseStabilizer stabilizer =
            new TrafficPhaseStabilizer("NORTH_SOUTH");

        String phase = stabilizer.updatePhase(
            "EAST_WEST", 10);

        assertEquals("NORTH_SOUTH", phase);
        assertEquals(10,
            stabilizer.getElapsedGreenTime());
    }

    @Test
    void testSwitchAfterMinimumDuration() {
        TrafficPhaseStabilizer stabilizer =
            new TrafficPhaseStabilizer("NORTH_SOUTH");

        stabilizer.updatePhase("EAST_WEST", 10);

        String phase = stabilizer.updatePhase(
            "EAST_WEST", 5);

        assertEquals("EAST_WEST", phase);
        assertEquals(0,
            stabilizer.getElapsedGreenTime());
    }

    @Test
    void testSamePhaseDoesNotResetTimer() {
        TrafficPhaseStabilizer stabilizer =
            new TrafficPhaseStabilizer("NORTH_SOUTH");

        stabilizer.updatePhase("NORTH_SOUTH", 8);
        stabilizer.updatePhase("NORTH_SOUTH", 7);

        assertEquals("NORTH_SOUTH",
            stabilizer.getCurrentPhase());

        assertEquals(15,
            stabilizer.getElapsedGreenTime());
    }

    @Test
    void testInvalidPhase() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new TrafficPhaseStabilizer("INVALID")
        );
    }

    @Test
    void testNegativeElapsedTime() {
        TrafficPhaseStabilizer stabilizer =
            new TrafficPhaseStabilizer("NORTH_SOUTH");

        assertThrows(
            IllegalArgumentException.class,
            () -> stabilizer.updatePhase(
                "EAST_WEST", -1)
        );
    }
}
