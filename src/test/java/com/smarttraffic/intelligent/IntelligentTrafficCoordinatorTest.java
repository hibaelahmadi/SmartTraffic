
package com.smarttraffic.intelligent;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IntelligentTrafficCoordinatorTest {

    // Faux contrôleur utilisé uniquement pour les tests.
    private static class FakeTrafficLightController
            implements TrafficPhaseRequestHandler {

        private String currentPhase = "NORTH_SOUTH";
        private String lastRequestedPhase = null;
        private int requestCount = 0;

        @Override
        public void requestPhase(String phase) {
            lastRequestedPhase = phase;
            requestCount++;
        }

        @Override
        public String getCurrentPhase() {
            return currentPhase;
        }

        public String getLastRequestedPhase() {
            return lastRequestedPhase;
        }

        public int getRequestCount() {
            return requestCount;
        }
    }

    @Test
    void testNoSwitchBefore15Seconds() {

        FakeTrafficLightController fake =
            new FakeTrafficLightController();

        IntelligentTrafficCoordinator coordinator =
            new IntelligentTrafficCoordinator(
                fake, "NORTH_SOUTH"
            );

        String phase = coordinator.updateTraffic(
            5, 10,
            5, 10,
            25, 40,
            20, 35,
            30,
            10
        );

        assertEquals("NORTH_SOUTH", phase);
        assertEquals(0, fake.getRequestCount());
    }

    @Test
    void testRequestSwitchAfter15Seconds() {

        FakeTrafficLightController fake =
            new FakeTrafficLightController();

        IntelligentTrafficCoordinator coordinator =
            new IntelligentTrafficCoordinator(
                fake, "NORTH_SOUTH"
            );

        coordinator.updateTraffic(
            5, 10,
            5, 10,
            25, 40,
            20, 35,
            30,
            10
        );

        String phase = coordinator.updateTraffic(
            5, 10,
            5, 10,
            25, 40,
            20, 35,
            30,
            5
        );

        assertEquals("EAST_WEST", phase);
        assertEquals(1, fake.getRequestCount());
        assertEquals(
            "EAST_WEST",
            fake.getLastRequestedPhase()
        );
    }

    @Test
    void testNoRequestWhenPhaseUnchanged() {

        FakeTrafficLightController fake =
            new FakeTrafficLightController();

        IntelligentTrafficCoordinator coordinator =
            new IntelligentTrafficCoordinator(
                fake, "NORTH_SOUTH"
            );

        coordinator.updateTraffic(
            25, 40,
            20, 35,
            5, 10,
            5, 10,
            30,
            20
        );

        assertEquals(0, fake.getRequestCount());
        assertEquals(
            "NORTH_SOUTH",
            coordinator.getRequestedPhase()
        );
    }

    @Test
    void testActualPhaseRemainsUnchangedUntilConfirmed() {

        FakeTrafficLightController fake =
            new FakeTrafficLightController();

        IntelligentTrafficCoordinator coordinator =
            new IntelligentTrafficCoordinator(
                fake, "NORTH_SOUTH"
            );

        coordinator.updateTraffic(
            5, 10,
            5, 10,
            25, 40,
            20, 35,
            30,
            15
        );

        assertEquals(
            "EAST_WEST",
            coordinator.getRequestedPhase()
        );

        assertEquals(
            "NORTH_SOUTH",
            coordinator.getActualPhase()
        );
    }

    @Test
    void testNullHandlerRejected() {

        assertThrows(
            IllegalArgumentException.class,
            () -> new IntelligentTrafficCoordinator(
                null, "NORTH_SOUTH"
            )
        );
    }

    @Test
    void testNoDuplicateRequestsWhilePending() {

        FakeTrafficLightController fake =
            new FakeTrafficLightController();

        IntelligentTrafficCoordinator coordinator =
            new IntelligentTrafficCoordinator(
                fake, "NORTH_SOUTH"
            );

        // Premiere demande apres 15 secondes.
        coordinator.updateTraffic(
            5, 10, 5, 10,
            25, 40, 20, 35,
            30, 15
        );

        // La phase reelle n'a pas encore change.
        coordinator.updateTraffic(
            5, 10, 5, 10,
            25, 40, 20, 35,
            30, 5
        );

        assertEquals(1, fake.getRequestCount());
        assertEquals("EAST_WEST",
            coordinator.getRequestedPhase());

        assertEquals("NORTH_SOUTH",
            coordinator.getActualPhase());
    }

    @Test
    void testPendingRequestConfirmed() {

        FakeTrafficLightController fake =
            new FakeTrafficLightController();

        IntelligentTrafficCoordinator coordinator =
            new IntelligentTrafficCoordinator(
                fake, "NORTH_SOUTH"
            );

        coordinator.updateTraffic(
            5, 10, 5, 10,
            25, 40, 20, 35,
            30, 15
        );

        // Simuler la confirmation du controleur.
        fake.currentPhase = "EAST_WEST";

        coordinator.updateTraffic(
            5, 10, 5, 10,
            25, 40, 20, 35,
            30, 0
        );

        assertEquals("EAST_WEST",
            coordinator.getActualPhase());

        assertEquals("EAST_WEST",
            coordinator.getRequestedPhase());

        assertEquals(1, fake.getRequestCount());
    }

    @Test
    void testMinimumGreenAfterConfirmation() {

        FakeTrafficLightController fake =
            new FakeTrafficLightController();

        IntelligentTrafficCoordinator coordinator =
            new IntelligentTrafficCoordinator(
                fake, "NORTH_SOUTH"
            );

        // Demander Est-Ouest.
        coordinator.updateTraffic(
            5, 10, 5, 10,
            25, 40, 20, 35,
            30, 15
        );

        // Confirmer le passage a Est-Ouest.
        fake.currentPhase = "EAST_WEST";

        coordinator.updateTraffic(
            5, 10, 5, 10,
            25, 40, 20, 35,
            30, 0
        );

        // Nord-Sud redevient prioritaire,
        // mais seulement 10 secondes se sont ecoulees.
        coordinator.updateTraffic(
            25, 40, 20, 35,
            5, 10, 5, 10,
            30, 10
        );

        assertEquals(1, fake.getRequestCount());

        // Apres 5 secondes supplementaires,
        // le changement devient admissible.
        coordinator.updateTraffic(
            25, 40, 20, 35,
            5, 10, 5, 10,
            30, 5
        );

        assertEquals(2, fake.getRequestCount());

        assertEquals("NORTH_SOUTH",
            fake.getLastRequestedPhase());
    }
}
