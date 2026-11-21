
package com.smarttraffic.intelligent;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmergencyPriorityManagerTest {

    @Test
    void testNoEmergencyInitially() {
        EmergencyPriorityManager manager =
            new EmergencyPriorityManager();

        assertFalse(manager.hasEmergency());
    }

    @Test
    void testAmbulancePriority() {
        EmergencyPriorityManager manager =
            new EmergencyPriorityManager();

        manager.requestPriority("NORD");

        assertTrue(manager.hasEmergency());
        assertTrue(manager.hasPriority("NORD"));
        assertFalse(manager.hasPriority("SUD"));
    }

    @Test
    void testClearPriority() {
        EmergencyPriorityManager manager =
            new EmergencyPriorityManager();

        manager.requestPriority("EST");
        manager.clearPriority();

        assertFalse(manager.hasEmergency());
        assertNull(manager.getEmergencyLane());
    }

    @Test
    void testInvalidLane() {
        EmergencyPriorityManager manager =
            new EmergencyPriorityManager();

        assertThrows(
            IllegalArgumentException.class,
            () -> manager.requestPriority("")
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> manager.requestPriority(null)
        );
    }
}
