package model.light;

import static org.junit.jupiter.api.Assertions.*;

import common.Direction;
import common.LightState;
import org.junit.jupiter.api.Test;

class TrafficLightTest {

    @Test
    void timerAccumulatesWithUpdate() {
        TrafficLight light = new TrafficLight(Direction.NORD, LightState.ROUGE);
        light.update(2.5);
        light.update(1.5);
        assertEquals(4.0, light.getTimeInState(), 1e-9);
    }

    @Test
    void changingStateResetsTimer() {
        TrafficLight light = new TrafficLight(Direction.NORD, LightState.ROUGE);
        light.update(10);
        light.setState(LightState.VERT);
        assertEquals(LightState.VERT, light.getState());
        assertEquals(0.0, light.getTimeInState(), 1e-9);
    }

    @Test
    void settingSameStateKeepsTimer() {
        TrafficLight light = new TrafficLight(Direction.EST, LightState.VERT);
        light.update(7);
        light.setState(LightState.VERT);
        assertEquals(7.0, light.getTimeInState(), 1e-9);
    }
}
