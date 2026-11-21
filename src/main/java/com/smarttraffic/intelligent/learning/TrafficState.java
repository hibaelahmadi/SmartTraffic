package com.smarttraffic.intelligent.learning;

public class TrafficState {

    private final int vehiclesNS;
    private final int vehiclesEW;
    private final int waitingTimeNS;
    private final int waitingTimeEW;

    public TrafficState(int vehiclesNS, int vehiclesEW,
                        int waitingTimeNS, int waitingTimeEW) {
        if (vehiclesNS < 0 || vehiclesEW < 0
            || waitingTimeNS < 0 || waitingTimeEW < 0) {

            throw new IllegalArgumentException(
                "Les données du trafic doivent être positives ou nulles."
            );
        }

        this.vehiclesNS = vehiclesNS;
        this.vehiclesEW = vehiclesEW;
        this.waitingTimeNS = waitingTimeNS;
        this.waitingTimeEW = waitingTimeEW;
    }

    public int getVehiclesNS() {
        return vehiclesNS;
    }

    public int getVehiclesEW() {
        return vehiclesEW;
    }

    public int getWaitingTimeNS() {
        return waitingTimeNS;
    }

    public int getWaitingTimeEW() {
        return waitingTimeEW;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof TrafficState other)) {
            return false;
        }

        return vehiclesNS == other.vehiclesNS
            && vehiclesEW == other.vehiclesEW
            && waitingTimeNS == other.waitingTimeNS
            && waitingTimeEW == other.waitingTimeEW;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(
            vehiclesNS, vehiclesEW,
            waitingTimeNS, waitingTimeEW
        );
    }
    public int getTotalVehicles() {
        return vehiclesNS + vehiclesEW;
    }

    public int getTotalWaitingTime() {
        return waitingTimeNS + waitingTimeEW;
    }
    public double getAverageWaitingTime() {
        int totalVehicles = getTotalVehicles();

        if (totalVehicles == 0) {
            return 0.0;
        }

        return (double) getTotalWaitingTime() / totalVehicles;
    }
    @Override
    public String toString() {
        return "TrafficState{" +
            "vehiclesNS=" + vehiclesNS +
            ", vehiclesEW=" + vehiclesEW +
            ", waitingTimeNS=" + waitingTimeNS +
            ", waitingTimeEW=" + waitingTimeEW +
            '}';
    }
}