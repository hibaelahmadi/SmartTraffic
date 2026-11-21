
package com.smarttraffic.intelligent;

public class IntelligentTrafficCoordinator {

    private final TrafficPhaseSelector selector;
    private final TrafficPhaseRequestHandler handler;

    private String pendingPhase;
    private String trackedActualPhase;
    private int elapsedGreenTime;

    private static final int MIN_GREEN_DURATION = 15;

    public IntelligentTrafficCoordinator(
            TrafficPhaseRequestHandler handler,
            String initialPhase) {

        if (handler == null) {
            throw new IllegalArgumentException(
                "Le gestionnaire des feux est obligatoire."
            );
        }

        if (!isValidPhase(initialPhase)) {
            throw new IllegalArgumentException(
                "Phase initiale invalide."
            );
        }

        if (!initialPhase.equals(handler.getCurrentPhase())) {
            throw new IllegalArgumentException(
                "La phase initiale ne correspond pas "
                + "au controleur des feux."
            );
        }

        this.selector = new TrafficPhaseSelector();
        this.handler = handler;
        this.trackedActualPhase = initialPhase;
        this.elapsedGreenTime = 0;
    }

    public String updateTraffic(
            int northVehicles, double northWait,
            int southVehicles, double southWait,
            int eastVehicles, double eastWait,
            int westVehicles, double westWait,
            int capacity,
            int elapsedSeconds) {

        if (elapsedSeconds < 0) {
            throw new IllegalArgumentException(
                "Le temps ecoule ne peut pas etre negatif."
            );
        }

        String preferredPhase = selector.selectPriorityPhase(
            northVehicles, northWait,
            southVehicles, southWait,
            eastVehicles, eastWait,
            westVehicles, westWait,
            capacity
        );

        String actualPhase = handler.getCurrentPhase();

        if (!isValidPhase(actualPhase)) {
            throw new IllegalStateException(
                "Phase reelle inconnue ou en transition."
            );
        }

        // Detecter la confirmation d'une nouvelle phase.
        if (!actualPhase.equals(trackedActualPhase)) {
            trackedActualPhase = actualPhase;
            elapsedGreenTime = 0;
        } else {
            elapsedGreenTime += elapsedSeconds;
        }

        // Une demande est deja en attente.
        if (pendingPhase != null) {

            if (pendingPhase.equals(actualPhase)) {
                pendingPhase = null;
            } else {
                return pendingPhase;
            }
        }

        // Ne pas interrompre le vert trop tot.
        if (elapsedGreenTime < MIN_GREEN_DURATION) {
            return actualPhase;
        }

        // Demander une nouvelle phase si necessaire.
        if (!preferredPhase.equals(actualPhase)) {
            handler.requestPhase(preferredPhase);
            pendingPhase = preferredPhase;
            return preferredPhase;
        }

        return actualPhase;
    }

    public String getRequestedPhase() {
        return pendingPhase != null
            ? pendingPhase
            : handler.getCurrentPhase();
    }

    public String getActualPhase() {
        return handler.getCurrentPhase();
    }

    private boolean isValidPhase(String phase) {
        return "NORTH_SOUTH".equals(phase)
            || "EAST_WEST".equals(phase);
    }
}
