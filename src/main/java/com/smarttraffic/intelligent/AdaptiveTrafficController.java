
package com.smarttraffic.intelligent;

public class AdaptiveTrafficController {

    private static final int MIN_GREEN = 15;
    private static final int MAX_GREEN = 45;

    private final CongestionCalculator calculator;
    private final EmergencyPriorityManager emergencyManager;

    public AdaptiveTrafficController() {
        this.calculator = new CongestionCalculator();
        this.emergencyManager = new EmergencyPriorityManager();
    }

    public int calculateGreenDuration(TrafficSensor sensor) {

        if (sensor == null) {
            throw new IllegalArgumentException(
                "Le capteur ne peut pas être null."
            );
        }

        int vehicles = sensor.getVehicleCount();

        return Math.max(
            MIN_GREEN,
            Math.min(MAX_GREEN, MIN_GREEN + vehicles)
        );
    }

    public String analyzeTraffic(
            TrafficSensor sensor, int capacity) {

        if (sensor == null) {
            throw new IllegalArgumentException(
                "Le capteur ne peut pas être null."
            );
        }

        double congestion =
            calculator.calculateCongestion(
                sensor.getVehicleCount(), capacity
            );

        return calculator.getCongestionLevel(congestion);
    }

    public void requestEmergencyPriority(String laneId) {
        emergencyManager.requestPriority(laneId);
    }

    public boolean shouldPrioritize(String laneId) {
        return emergencyManager.hasPriority(laneId);
    }

    public boolean hasEmergency() {
        return emergencyManager.hasEmergency();
    }

    public void clearEmergencyPriority() {
        emergencyManager.clearPriority();
    }
}
