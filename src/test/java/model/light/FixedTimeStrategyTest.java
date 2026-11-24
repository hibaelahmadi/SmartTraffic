package model.light;

import static org.junit.jupiter.api.Assertions.*;

import common.Direction;
import common.LightState;
import common.TrafficStrategy;
import org.junit.jupiter.api.Test;

class FixedTimeStrategyTest {

    @Test
    void canBeUsedThroughTheTrafficStrategyInterface() {
        TrafficStrategy strategy = new FixedTimeStrategy();
        assertEquals(LightState.VERT, strategy.getStateFor(Direction.NORD));
        assertEquals(LightState.ROUGE, strategy.getStateFor(Direction.EST));

        strategy.update(FakeIntersectionState.noAmbulance(), 35);
        assertEquals(LightState.ROUGE, strategy.getStateFor(Direction.NORD));
        assertEquals(LightState.VERT, strategy.getStateFor(Direction.EST));
    }

    @Test
    void customTimingIsUsed() {
        TrafficStrategy strategy = new FixedTimeStrategy(new LightTiming(10, 2));
        strategy.update(FakeIntersectionState.noAmbulance(), 10);
        assertEquals(LightState.ORANGE, strategy.getStateFor(Direction.NORD));
    }

    @Test
    void fixedModeIgnoresTrafficVolume() {
        // Deux intersections avec un trafic très différent doivent avoir exactement les mêmes feux
        TrafficStrategy heavyNorth = new FixedTimeStrategy();
        TrafficStrategy heavyEast = new FixedTimeStrategy();
        FakeIntersectionState northJam = FakeIntersectionState.noAmbulance()
                .withWaiting(Direction.NORD, 50).withWaiting(Direction.SUD, 40);
        FakeIntersectionState eastJam = FakeIntersectionState.noAmbulance()
                .withWaiting(Direction.EST, 50).withWaiting(Direction.OUEST, 40);

        for (int second = 0; second < 300; second++) {
            heavyNorth.update(northJam, 1);
            heavyEast.update(eastJam, 1);
            for (Direction d : Direction.values()) {
                assertEquals(heavyNorth.getStateFor(d), heavyEast.getStateFor(d),
                        "Les feux dépendent du trafic à la seconde " + second);
            }
        }
    }
}
