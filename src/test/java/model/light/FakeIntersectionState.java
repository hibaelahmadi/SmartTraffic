package model.light;

import common.Direction;
import common.IntersectionState;

import java.util.EnumMap;
import java.util.Map;

/** Faux état d'intersection pour tester sans dépendre des autres modules. */
class FakeIntersectionState implements IntersectionState {
    private final Direction ambulanceDirection;
    private final Map<Direction, Integer> waiting = new EnumMap<>(Direction.class);

    private FakeIntersectionState(Direction ambulanceDirection) {
        this.ambulanceDirection = ambulanceDirection;
    }

    static FakeIntersectionState noAmbulance() {
        return new FakeIntersectionState(null);
    }

    static FakeIntersectionState ambulanceFrom(Direction d) {
        return new FakeIntersectionState(d);
    }

    /** Configure le nombre de véhicules en attente dans une direction. */
    FakeIntersectionState withWaiting(Direction d, int count) {
        waiting.put(d, count);
        return this;
    }

    @Override public int getWaitingCount(Direction d) { return waiting.getOrDefault(d, 0); }
    @Override public double getTotalWaitTime(Direction d) { return getWaitingCount(d) * 10.0; }
    @Override public double getMaxWaitTime(Direction d) { return getWaitingCount(d) * 2.0; }
    @Override public boolean hasAmbulanceApproaching() { return ambulanceDirection != null; }
    @Override public Direction getAmbulanceDirection() { return ambulanceDirection; }
}
