package com.smarttraffic.intelligent.learning;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrafficSimulationEnvironmentTest {

    @Test
    void testInitialState() {
        TrafficState initialState =
            new TrafficState(10, 20, 5, 15);

        TrafficSimulationEnvironment environment =
            new TrafficSimulationEnvironment(initialState);

        assertEquals(
            initialState,
            environment.getCurrentState()
        );
    }
    @Test
    void testResetEnvironment() {
        TrafficState initialState =
            new TrafficState(10, 20, 5, 15);

        TrafficState newState =
            new TrafficState(30, 10, 20, 5);

        TrafficSimulationEnvironment environment =
            new TrafficSimulationEnvironment(initialState);

        environment.reset(newState);

        assertEquals(newState, environment.getCurrentState());
    }
    @Test
    void testNullInitialState() {
        assertThrows(IllegalArgumentException.class, () ->
            new TrafficSimulationEnvironment(null)
        );
    }

    @Test
    void testResetWithNullState() {
        TrafficState initialState =
            new TrafficState(10, 20, 5, 15);

        TrafficSimulationEnvironment environment =
            new TrafficSimulationEnvironment(initialState);

        assertThrows(IllegalArgumentException.class, () ->
            environment.reset(null)
        );

        assertEquals(initialState, environment.getCurrentState());
    }
    @Test
    void testNegativeVehicleCount() {
        assertThrows(IllegalArgumentException.class, () ->
            new TrafficState(-10, 20, 5, 15)
        );
    }

    @Test
    void testNegativeWaitingTime() {
        assertThrows(IllegalArgumentException.class, () ->
            new TrafficState(10, 20, -5, 15)
        );
    }
    @Test
    void testTotalVehicles() {
        TrafficState state = new TrafficState(10, 20, 5, 15);

        assertEquals(30, state.getTotalVehicles());
    }

    @Test
    void testTotalWaitingTime() {
        TrafficState state = new TrafficState(10, 20, 5, 15);

        assertEquals(20, state.getTotalWaitingTime());
    }
    @Test
    void testAverageWaitingTime() {
        TrafficState state = new TrafficState(10, 20, 30, 60);

        assertEquals(3.0, state.getAverageWaitingTime(), 0.0001);
    }

    @Test
    void testAverageWaitingTimeWithoutVehicles() {
        TrafficState state = new TrafficState(0, 0, 0, 0);

        assertEquals(0.0, state.getAverageWaitingTime());
    }
    @Test
    void testWaitingTimeDecrease() {
        TrafficState initialState = new TrafficState(10, 20, 30, 40);
        TrafficState nextState = new TrafficState(8, 15, 20, 30);

        TrafficSimulationEnvironment environment =
            new TrafficSimulationEnvironment(initialState);

        assertEquals(-20, environment.getWaitingTimeVariation(nextState));
    }

    @Test
    void testWaitingTimeIncrease() {
        TrafficState initialState = new TrafficState(10, 20, 30, 40);
        TrafficState nextState = new TrafficState(15, 25, 40, 50);

        TrafficSimulationEnvironment environment =
            new TrafficSimulationEnvironment(initialState);

        assertEquals(20, environment.getWaitingTimeVariation(nextState));
    }
    @Test
    void testNoVariation() {
        TrafficState state = new TrafficState(10, 20, 30, 40);

        TrafficSimulationEnvironment environment =
            new TrafficSimulationEnvironment(state);

        assertEquals(0, environment.getWaitingTimeVariation(state));
        assertEquals(0, environment.getVehicleCountVariation(state));
    }
    @Test
    void testWaitingTimeVariationWithNull() {
        TrafficSimulationEnvironment environment =
            new TrafficSimulationEnvironment(
                new TrafficState(10, 20, 30, 40)
            );

        assertThrows(IllegalArgumentException.class, () ->
            environment.getWaitingTimeVariation(null)
        );
    }

    @Test
    void testVehicleCountVariationWithNull() {
        TrafficSimulationEnvironment environment =
            new TrafficSimulationEnvironment(
                new TrafficState(10, 20, 30, 40)
            );

        assertThrows(IllegalArgumentException.class, () ->
            environment.getVehicleCountVariation(null)
        );
    }
    @Test
    void testVehicleCountDecrease() {
        TrafficState initialState = new TrafficState(10, 20, 30, 40);
        TrafficState nextState = new TrafficState(8, 15, 20, 30);

        TrafficSimulationEnvironment environment =
            new TrafficSimulationEnvironment(initialState);

        assertEquals(-7, environment.getVehicleCountVariation(nextState));
    }

    @Test
    void testVehicleCountIncrease() {
        TrafficState initialState = new TrafficState(10, 20, 30, 40);
        TrafficState nextState = new TrafficState(15, 25, 40, 50);

        TrafficSimulationEnvironment environment =
            new TrafficSimulationEnvironment(initialState);

        assertEquals(10, environment.getVehicleCountVariation(nextState));
    }
    @Test
    void testTrafficStateToString() {
        TrafficState state = new TrafficState(10, 20, 5, 15);

        assertEquals(
            "TrafficState{vehiclesNS=10, vehiclesEW=20, "
            + "waitingTimeNS=5, waitingTimeEW=15}",
            state.toString()
        );
    }
}