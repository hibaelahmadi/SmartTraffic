
package com.smarttraffic.intelligent;

public class TrafficPhaseSelector {

    private final TrafficPriorityCalculator calculator;

    public TrafficPhaseSelector() {
        this.calculator = new TrafficPriorityCalculator();
    }

    public String selectPriorityPhase(
            int northVehicles, double northWait,
            int southVehicles, double southWait,
            int eastVehicles, double eastWait,
            int westVehicles, double westWait,
            int capacity) {

        double northScore = calculator.calculatePriority(
            northVehicles, capacity, northWait);

        double southScore = calculator.calculatePriority(
            southVehicles, capacity, southWait);

        double eastScore = calculator.calculatePriority(
            eastVehicles, capacity, eastWait);

        double westScore = calculator.calculatePriority(
            westVehicles, capacity, westWait);

        double northSouthScore = northScore + southScore;
        double eastWestScore = eastScore + westScore;

        if (northSouthScore >= eastWestScore) {
            return "NORTH_SOUTH";
        }

        return "EAST_WEST";
    }
}
