
package com.smarttraffic.intelligent;

public class TrafficSensor {

    private final String laneId;
    private int vehicleCount;

    public TrafficSensor(String laneId) {
        this.laneId = laneId;
        this.vehicleCount = 0;
    }

    public String getLaneId() {
        return laneId;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public void updateVehicleCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException(
                "Le nombre de véhicules ne peut pas être négatif."
            );
        }

        this.vehicleCount = count;
    }
}
