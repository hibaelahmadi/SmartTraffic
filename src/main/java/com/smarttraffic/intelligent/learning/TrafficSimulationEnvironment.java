package com.smarttraffic.intelligent.learning;

public class TrafficSimulationEnvironment {

    private TrafficState currentState;

    public TrafficSimulationEnvironment(TrafficState initialState) {
        reset(initialState);
    }

    public TrafficState getCurrentState() {
        return currentState;
    }

    public void reset(TrafficState initialState) {
        if (initialState == null) {
            throw new IllegalArgumentException(
                "L'état initial ne peut pas être null."
            );
        }

        this.currentState = initialState;
    }

    public int getWaitingTimeVariation(TrafficState nextState) {
        if (nextState == null) {
            throw new IllegalArgumentException(
                "L'état suivant ne peut pas être null."
            );
        }

        return nextState.getTotalWaitingTime()
            - currentState.getTotalWaitingTime();
    }

    public int getVehicleCountVariation(TrafficState nextState) {
        if (nextState == null) {
            throw new IllegalArgumentException(
                "L'état suivant ne peut pas être null."
            );
        }

        return nextState.getTotalVehicles()
            - currentState.getTotalVehicles();
    }
}