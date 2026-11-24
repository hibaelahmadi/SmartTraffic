package model.light;

import common.Direction;
import common.LightState;

/**
 * Un feu de circulation : une direction, un état et un chronomètre interne.
 *
 * <p>Le chronomètre mesure le temps écoulé depuis le dernier changement d'état (utile pour
 * l'affichage ou les statistiques). Le rythme du cycle, lui, est piloté par
 * {@link IntersectionController} (champ {@code timeInPhase}), car les quatre feux
 * d'une intersection doivent changer ensemble.
 */
public class TrafficLight {
    private final Direction direction;
    private LightState state;
    private double timeInState;

    public TrafficLight(Direction direction, LightState initialState) {
        this.direction = direction;
        this.state = initialState;
    }

    /** Fait avancer le chronomètre de dt secondes. */
    public void update(double dt) {
        timeInState += dt;
    }

    /** Change l'état ; le chronomètre repart à zéro seulement si l'état change. */
    public void setState(LightState newState) {
        if (newState != state) {
            state = newState;
            timeInState = 0;
        }
    }

    public Direction getDirection() { return direction; }
    public LightState getState() { return state; }
    public double getTimeInState() { return timeInState; }
}
