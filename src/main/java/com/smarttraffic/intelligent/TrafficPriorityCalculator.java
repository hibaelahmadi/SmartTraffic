
package com.smarttraffic.intelligent;

public class TrafficPriorityCalculator {

    private static final double CONGESTION_WEIGHT = 0.6;
    private static final double WAITING_WEIGHT = 0.4;

    private static final double MAX_WAITING_TIME = 120.0;

    public double calculatePriority(
            int vehicleCount,
            int capacity,
            double waitingTime) {

        if (vehicleCount < 0 || capacity <= 0
                || !Double.isFinite(waitingTime)
                || waitingTime < 0) {
            throw new IllegalArgumentException(
                "Parametres invalides."
            );
        }

        double congestion = Math.min(
            1.0,
            (double) vehicleCount / capacity
        );

        double waiting = Math.min(
            1.0,
            waitingTime / MAX_WAITING_TIME
        );

        return CONGESTION_WEIGHT * congestion
                + WAITING_WEIGHT * waiting;
    }
}
