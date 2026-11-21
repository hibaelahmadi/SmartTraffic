
package com.smarttraffic.intelligent;

public class CongestionCalculator {

    public double calculateCongestion(
            int vehicleCount, int capacity) {

        if (vehicleCount < 0 || capacity <= 0) {
            throw new IllegalArgumentException(
                "Nombre de véhicules ou capacité invalide."
            );
        }

        return (vehicleCount * 100.0) / capacity;
    }

    public String getCongestionLevel(double congestion) {

        if (congestion < 0) {
            throw new IllegalArgumentException(
                "La congestion ne peut pas être négative."
            );
        }

        if (congestion < 30) {
            return "FAIBLE";
        } else if (congestion < 70) {
            return "MOYENNE";
        } else {
            return "FORTE";
        }
    }
}
