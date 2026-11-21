
package com.smarttraffic.intelligent;

public class EmergencyPriorityManager {

    private String emergencyLane;

    public void requestPriority(String laneId) {

        if (laneId == null || laneId.isBlank()) {
            throw new IllegalArgumentException(
                "La voie de l'ambulance est invalide."
            );
        }

        emergencyLane = laneId;
    }

    public boolean hasEmergency() {
        return emergencyLane != null;
    }

    public boolean hasPriority(String laneId) {
        return emergencyLane != null
                && emergencyLane.equals(laneId);
    }

    public String getEmergencyLane() {
        return emergencyLane;
    }

    public void clearPriority() {
        emergencyLane = null;
    }
}
