
package com.smarttraffic.intelligent;

public class TrafficPhaseStabilizer {

    private static final int MIN_GREEN_DURATION = 15;

    private String currentPhase;
    private int elapsedGreenTime;

    public TrafficPhaseStabilizer(String initialPhase) {

        if (!isValidPhase(initialPhase)) {
            throw new IllegalArgumentException(
                "Phase initiale invalide."
            );
        }

        this.currentPhase = initialPhase;
        this.elapsedGreenTime = 0;
    }

    public String updatePhase(
            String requestedPhase,
            int elapsedSeconds) {

        if (!isValidPhase(requestedPhase)
                || elapsedSeconds < 0) {
            throw new IllegalArgumentException(
                "Parametres invalides."
            );
        }

        elapsedGreenTime += elapsedSeconds;

        if (!currentPhase.equals(requestedPhase)
                && elapsedGreenTime >= MIN_GREEN_DURATION) {

            currentPhase = requestedPhase;
            elapsedGreenTime = 0;
        }

        return currentPhase;
    }

    public String getCurrentPhase() {
        return currentPhase;
    }

    public int getElapsedGreenTime() {
        return elapsedGreenTime;
    }

    private boolean isValidPhase(String phase) {
        return "NORTH_SOUTH".equals(phase)
                || "EAST_WEST".equals(phase);
    }
}
