package com.smarttraffic.intelligent.learning;

import java.util.HashMap;
import java.util.Map;

public class QLearningAgent {

    private final Map<TrafficState, Map<TrafficAction, Double>> qTable;

    public QLearningAgent() {
        this.qTable = new HashMap<>();
    }
    public double getQValue(TrafficState state, TrafficAction action) {
        return qTable
            .getOrDefault(state, Map.of())
            .getOrDefault(action, 0.0);
    }
    public void setQValue(TrafficState state,
                      TrafficAction action,
                      double value) {

        qTable.computeIfAbsent(state, key -> new HashMap<>())
            .put(action, value);
    }
    public double getMaxQValue(TrafficState state) {
        double max = Double.NEGATIVE_INFINITY;

        for (TrafficAction action : TrafficAction.values()) {
            max = Math.max(max, getQValue(state, action));
        }

        return max;
    }
    public void updateQValue(TrafficState state,
                         TrafficAction action,
                         double reward,
                         TrafficState nextState,
                         double alpha,
                         double gamma) {

        if (!Double.isFinite(reward)) {
            throw new IllegalArgumentException(
                "La récompense doit être une valeur finie."
            );
        }
        if (!Double.isFinite(alpha) || alpha < 0 || alpha > 1) {
            throw new IllegalArgumentException(
                "Alpha doit être compris entre 0 et 1."
            );
        }

        if (!Double.isFinite(gamma) || gamma < 0 || gamma > 1) {
            throw new IllegalArgumentException(
                "Gamma doit être compris entre 0 et 1."
            );
        }

        double oldQ = getQValue(state, action);
        double maxNextQ = getMaxQValue(nextState);

        double newQ = oldQ + alpha *
            (reward + gamma * maxNextQ - oldQ);

        setQValue(state, action, newQ);
    }
    public TrafficAction getBestAction(TrafficState state) {
        TrafficAction bestAction = TrafficAction.KEEP_PHASE;
        double bestValue = getQValue(state, bestAction);

        for (TrafficAction action : TrafficAction.values()) {
            double value = getQValue(state, action);

            if (value > bestValue) {
                bestValue = value;
                bestAction = action;
            }
        }

        return bestAction;
    }
}