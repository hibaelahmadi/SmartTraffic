
package com.smarttraffic.intelligent;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrafficPriorityCalculatorTest {

    private final TrafficPriorityCalculator calculator =
            new TrafficPriorityCalculator();

    @Test
    void testNormalPriority() {
        double score = calculator.calculatePriority(
            25, 30, 40
        );

        assertEquals(0.633333, score, 0.00001);
    }

    @Test
    void testNoTraffic() {
        double score = calculator.calculatePriority(
            0, 30, 0
        );

        assertEquals(0.0, score, 0.00001);
    }

    @Test
    void testMaximumPriority() {
        double score = calculator.calculatePriority(
            50, 30, 180
        );

        assertEquals(1.0, score, 0.00001);
    }

    @Test
    void testWaitingTimeInfluence() {
        double shortWait = calculator.calculatePriority(
            10, 30, 10
        );

        double longWait = calculator.calculatePriority(
            10, 30, 90
        );

        assertTrue(longWait > shortWait);
    }

    @Test
    void testInvalidCapacity() {
        assertThrows(
            IllegalArgumentException.class,
            () -> calculator.calculatePriority(10, 0, 20)
        );
    }

    @Test
    void testNegativeWaitingTime() {
        assertThrows(
            IllegalArgumentException.class,
            () -> calculator.calculatePriority(10, 30, -5)
        );
    }
}
