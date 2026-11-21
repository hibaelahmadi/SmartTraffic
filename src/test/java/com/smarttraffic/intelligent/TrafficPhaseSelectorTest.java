
package com.smarttraffic.intelligent;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrafficPhaseSelectorTest {

    private final TrafficPhaseSelector selector =
            new TrafficPhaseSelector();

    @Test
    void testNorthSouthPriority() {

        String phase = selector.selectPriorityPhase(
            25, 40,
            20, 35,
            5, 10,
            3, 10,
            30
        );

        assertEquals("NORTH_SOUTH", phase);
    }

    @Test
    void testEastWestPriority() {

        String phase = selector.selectPriorityPhase(
            5, 10,
            3, 10,
            25, 40,
            20, 35,
            30
        );

        assertEquals("EAST_WEST", phase);
    }

    @Test
    void testWaitingTimeInfluence() {

        String phase = selector.selectPriorityPhase(
            10, 10,
            10, 10,
            10, 100,
            10, 100,
            30
        );

        assertEquals("EAST_WEST", phase);
    }

    @Test
    void testEqualPriority() {

        String phase = selector.selectPriorityPhase(
            10, 30,
            10, 30,
            10, 30,
            10, 30,
            30
        );

        assertEquals("NORTH_SOUTH", phase);
    }

    @Test
    void testInvalidCapacity() {

        assertThrows(
            IllegalArgumentException.class,
            () -> selector.selectPriorityPhase(
                10, 20,
                10, 20,
                10, 20,
                10, 20,
                0
            )
        );
    }
}
