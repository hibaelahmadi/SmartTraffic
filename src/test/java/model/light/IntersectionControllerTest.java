package model.light;

import static org.junit.jupiter.api.Assertions.*;

import common.Direction;
import common.LightState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

class IntersectionControllerTest {
    private IntersectionController controller;
    private final FakeIntersectionState none = FakeIntersectionState.noAmbulance();

    @BeforeEach
    void setUp() {
        controller = new IntersectionController(LightTiming.standard()); // 30 s vert, 5 s orange
    }

    private void assertAxis(LightState ns, LightState ew) {
        assertEquals(ns, controller.getStateFor(Direction.NORD));
        assertEquals(ns, controller.getStateFor(Direction.SUD));
        assertEquals(ew, controller.getStateFor(Direction.EST));
        assertEquals(ew, controller.getStateFor(Direction.OUEST));
    }

    @Test
    void startsWithNorthSouthGreen() {
        assertAxis(LightState.VERT, LightState.ROUGE);
    }

    @Test
    void followsTheFourPhaseCycle() {
        controller.update(none, 30);
        assertAxis(LightState.ORANGE, LightState.ROUGE);
        controller.update(none, 5);
        assertAxis(LightState.ROUGE, LightState.VERT);
        controller.update(none, 30);
        assertAxis(LightState.ROUGE, LightState.ORANGE);
        controller.update(none, 5);
        assertAxis(LightState.VERT, LightState.ROUGE);
    }

    @Test
    void bigDtCrossesSeveralPhases() {
        controller.update(none, 75); // 70 s = un cycle complet, il reste 5 s
        assertEquals(Phase.NS_VERT, controller.getPhase());
        assertEquals(5.0, controller.getTimeInPhase(), 1e-9);
        assertAxis(LightState.VERT, LightState.ROUGE);
    }

    @Test
    void axesAreNeverActiveTogether() {
        for (int i = 0; i < 3000; i++) {
            controller.update(none, 0.1);
            boolean nsActive = controller.getStateFor(Direction.NORD) != LightState.ROUGE;
            boolean ewActive = controller.getStateFor(Direction.EST) != LightState.ROUGE;
            assertFalse(nsActive && ewActive, "Les deux axes ont le feu en même temps au tick " + i);
            assertEquals(controller.getStateFor(Direction.NORD), controller.getStateFor(Direction.SUD));
            assertEquals(controller.getStateFor(Direction.EST), controller.getStateFor(Direction.OUEST));
        }
    }

    @Test
    void rejectsNegativeDt() {
        assertThrows(IllegalArgumentException.class, () -> controller.update(none, -1));
    }

    @Test
    void worksWithNullState() {
        controller.update(null, 30);
        assertAxis(LightState.ORANGE, LightState.ROUGE);
    }

    // ----- Priorité ambulance -----

    @Test
    void ambulanceFromOtherAxisGoesThroughOrangeThenGreen() {
        FakeIntersectionState amb = FakeIntersectionState.ambulanceFrom(Direction.EST);

        controller.update(amb, 0.1);                       // NS était vert -> orange
        assertAxis(LightState.ORANGE, LightState.ROUGE);

        controller.update(amb, 5);                         // fin de l'orange -> EO vert
        assertAxis(LightState.ROUGE, LightState.VERT);
    }

    @Test
    void greenIsHeldWhileAmbulanceIsPresent() {
        FakeIntersectionState amb = FakeIntersectionState.ambulanceFrom(Direction.EST);
        controller.update(amb, 0.1);
        controller.update(amb, 5);
        controller.update(amb, 100);                       // bien plus que 30 s
        assertAxis(LightState.ROUGE, LightState.VERT);
    }

    @Test
    void ambulanceOnAlreadyGreenAxisKeepsGreen() {
        controller.update(FakeIntersectionState.ambulanceFrom(Direction.NORD), 100);
        assertAxis(LightState.VERT, LightState.ROUGE);
    }

    @Test
    void ambulanceOnOrangeAxisBringsBackGreen() {
        controller.update(none, 31);                       // NS orange
        controller.update(FakeIntersectionState.ambulanceFrom(Direction.SUD), 0.1);
        assertAxis(LightState.VERT, LightState.ROUGE);
    }

    @Test
    void normalCycleResumesAfterAmbulance() {
        FakeIntersectionState amb = FakeIntersectionState.ambulanceFrom(Direction.EST);
        controller.update(amb, 0.1);
        controller.update(amb, 5);                         // EO vert
        controller.update(none, 30);                       // l'ambulance est passée : cycle normal
        assertAxis(LightState.ROUGE, LightState.ORANGE);
    }

    // ----- Symétrie : ambulance pendant que Est/Ouest est vert -----

    @Test
    void ambulanceFromNorthDuringEastWestGreenGoesThroughOrangeThenGreen() {
        controller.update(none, 35);                       // EO vert
        assertAxis(LightState.ROUGE, LightState.VERT);
        FakeIntersectionState amb = FakeIntersectionState.ambulanceFrom(Direction.NORD);

        controller.update(amb, 0.1);                       // EO vert -> orange
        assertAxis(LightState.ROUGE, LightState.ORANGE);

        controller.update(amb, 5);                         // fin de l'orange -> NS vert
        assertAxis(LightState.VERT, LightState.ROUGE);

        controller.update(amb, 100);                       // le vert est maintenu
        assertAxis(LightState.VERT, LightState.ROUGE);
    }

    @Test
    void ambulanceFromWestDuringEastWestGreenKeepsGreen() {
        controller.update(none, 35);                       // EO vert
        controller.update(FakeIntersectionState.ambulanceFrom(Direction.OUEST), 100);
        assertAxis(LightState.ROUGE, LightState.VERT);
    }

    @Test
    void ambulanceOnOrangeEastWestAxisBringsBackGreen() {
        controller.update(none, 66);                       // EO orange
        assertAxis(LightState.ROUGE, LightState.ORANGE);
        controller.update(FakeIntersectionState.ambulanceFrom(Direction.EST), 0.1);
        assertAxis(LightState.ROUGE, LightState.VERT);
    }

    // ----- Exclusion mutuelle avec ambulances aléatoires -----

    @Test
    void axesNeverActiveTogetherEvenWithRandomAmbulances() {
        Random rnd = new Random(42);                       // graine fixe : test reproductible
        Direction[] dirs = Direction.values();
        FakeIntersectionState current = FakeIntersectionState.noAmbulance();
        LightState[] previous = snapshot();

        for (int i = 0; i < 5000; i++) {
            if (rnd.nextInt(50) == 0) {                    // l'ambulance apparaît ou disparaît
                current = rnd.nextBoolean()
                        ? FakeIntersectionState.noAmbulance()
                        : FakeIntersectionState.ambulanceFrom(dirs[rnd.nextInt(dirs.length)]);
            }
            controller.update(current, rnd.nextDouble() * 0.3);

            boolean nsActive = controller.getStateFor(Direction.NORD) != LightState.ROUGE;
            boolean ewActive = controller.getStateFor(Direction.EST) != LightState.ROUGE;
            assertFalse(nsActive && ewActive, "Deux axes actifs en même temps au tick " + i);
            assertEquals(controller.getStateFor(Direction.NORD), controller.getStateFor(Direction.SUD));
            assertEquals(controller.getStateFor(Direction.EST), controller.getStateFor(Direction.OUEST));

            // Un vert n'est jamais coupé brutalement : il passe toujours par l'orange
            LightState[] now = snapshot();
            for (int d = 0; d < now.length; d++) {
                assertFalse(previous[d] == LightState.VERT && now[d] == LightState.ROUGE,
                        "Vert coupé sans orange au tick " + i + " (direction " + dirs[d] + ")");
            }
            previous = now;
        }
    }

    private LightState[] snapshot() {
        Direction[] dirs = Direction.values();
        LightState[] states = new LightState[dirs.length];
        for (int i = 0; i < dirs.length; i++) {
            states[i] = controller.getStateFor(dirs[i]);
        }
        return states;
    }
}
