package model.light;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LightTimingTest {

    @Test
    void standardTimingHasExpectedValues() {
        LightTiming t = LightTiming.standard();
        assertEquals(30, t.getGreen());
        assertEquals(5, t.getOrange());
        assertEquals(70, t.getCycleDuration());
    }

    @Test
    void rejectsZeroOrNegativeDurations() {
        assertThrows(InvalidTimingException.class, () -> new LightTiming(0, 5));
        assertThrows(InvalidTimingException.class, () -> new LightTiming(30, -1));
    }

    @Test
    void rejectsNaNAndInfinity() {
        assertThrows(InvalidTimingException.class, () -> new LightTiming(Double.NaN, 5));
        assertThrows(InvalidTimingException.class, () -> new LightTiming(30, Double.POSITIVE_INFINITY));
    }
}
