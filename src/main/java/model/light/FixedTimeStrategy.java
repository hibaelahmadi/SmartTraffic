package model.light;

import common.Direction;
import common.IntersectionState;
import common.LightState;
import common.TrafficStrategy;

/** Mode classique : durées fixes, indépendantes du nombre de voitures (sauf priorité ambulance). */
public class FixedTimeStrategy implements TrafficStrategy {
    private final IntersectionController controller;

    public FixedTimeStrategy() {
        this(LightTiming.standard());
    }

    public FixedTimeStrategy(LightTiming timing) {
        this.controller = new IntersectionController(timing);
    }

    @Override
    public void update(IntersectionState state, double dt) {
        controller.update(state, dt);
    }

    @Override
    public LightState getStateFor(Direction d) {
        return controller.getStateFor(d);
    }
}
