package model.light;

import common.Direction;
import common.IntersectionState;
import common.LightState;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Coordonne les quatre feux d'une intersection.
 * Un seul axe a le feu à la fois : Nord/Sud et Est/Ouest ne sont jamais verts ensemble.
 * Gère aussi la priorité des ambulances.
 *
 * <p>Choix de conception (à rappeler dans le rapport) :
 * <ul>
 *   <li><b>Rouge</b> : le rouge d'un axe dure (vert + orange) de l'autre axe, soit 35 s avec les
 *       valeurs par défaut (vert 30 s, orange 5 s), et non 30 s.</li>
 *   <li><b>Pas de temps « tout rouge »</b> entre la fin de l'orange d'un axe et le vert de l'autre :
 *       simplification acceptable pour une simulation.</li>
 *   <li><b>Phases</b> : une énumération {@link Phase} avec {@code next()} remplace le pattern State,
 *       qui est conseillé mais non obligatoire ; elle reste plus courte et lisible.</li>
 *   <li><b>Ambulance</b> : un vert n'est jamais coupé brutalement (passage par l'orange) ; un axe
 *       en orange qui reçoit une ambulance repasse directement au vert.</li>
 * </ul>
 */
public class IntersectionController {
    private final LightTiming timing;
    private final Map<Direction, TrafficLight> lights = new EnumMap<>(Direction.class);
    private Phase phase = Phase.NS_VERT;
    private double timeInPhase = 0;

    public IntersectionController(LightTiming timing) {
        this.timing = Objects.requireNonNull(timing, "timing");
        for (Direction d : Direction.values()) {
            lights.put(d, new TrafficLight(d, LightState.ROUGE));
        }
        applyLights();
    }

    /**
     * Fait avancer l'intersection de dt secondes.
     *
     * @param state état de l'intersection (peut être null : alors pas d'ambulance)
     * @param dt    temps écoulé en secondes (>= 0)
     */
    public void update(IntersectionState state, double dt) {
        if (dt < 0) {
            throw new IllegalArgumentException("dt doit être >= 0 : " + dt);
        }
        for (TrafficLight light : lights.values()) {
            light.update(dt);
        }

        Direction ambulance = null;
        if (state != null && state.hasAmbulanceApproaching()) {
            ambulance = state.getAmbulanceDirection();
        }

        // Si une ambulance impose son rythme, le cycle normal est suspendu pour ce tick
        if (ambulance != null && handleAmbulance(Axis.of(ambulance))) {
            applyLights();
            return;
        }

        advance(dt);
        applyLights();
    }

    /**
     * Priorité ambulance : on ne coupe jamais brutalement un vert, on passe par l'orange.
     *
     * @return true si le cycle normal est suspendu pour ce tick
     */
    private boolean handleAmbulance(Axis target) {
        Axis other = target.opposite();

        if (phase == Phase.green(target)) {
            timeInPhase = 0;                     // on garde le vert tant que l'ambulance est là
            return true;
        }
        if (phase == Phase.orange(target)) {
            phase = Phase.green(target);         // simplification : on repasse au vert
            timeInPhase = 0;
            return true;
        }
        if (phase == Phase.green(other)) {
            phase = Phase.orange(other);         // l'autre axe passe d'abord à l'orange
            timeInPhase = 0;
            return true;
        }
        return false;                            // orange de l'autre axe : on le laisse finir
    }

    /** Cycle normal : gère aussi un grand dt qui traverse plusieurs phases. */
    private void advance(double dt) {
        timeInPhase += dt;
        double duration = phase.duration(timing);
        while (timeInPhase >= duration) {
            timeInPhase -= duration;             // on garde le reste, on ne remet pas à 0
            phase = phase.next();
            duration = phase.duration(timing);
        }
    }

    private void applyLights() {
        for (Direction d : Direction.values()) {
            lights.get(d).setState(computeState(d));
        }
    }

    private LightState computeState(Direction d) {
        if (Axis.of(d) != phase.getAxis()) {
            return LightState.ROUGE;
        }
        return phase.isGreen() ? LightState.VERT : LightState.ORANGE;
    }

    public LightState getStateFor(Direction d) { return lights.get(d).getState(); }
    public Phase getPhase() { return phase; }
    public double getTimeInPhase() { return timeInPhase; }
    public LightTiming getTiming() { return timing; }
}
