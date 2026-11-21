package com.smarttraffic.intelligent.learning;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QLearningAgentTest {

    @Test
    void testInitialQValue() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(10, 20, 5, 15);

        assertEquals(0.0,
            agent.getQValue(state, TrafficAction.KEEP_PHASE));
    }

    @Test
    void testSetQValue() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(10, 20, 5, 15);

        agent.setQValue(state, TrafficAction.KEEP_PHASE, 0.8);

        assertEquals(0.8,
            agent.getQValue(state, TrafficAction.KEEP_PHASE));
    }
    @Test
    void testEquivalentStates() {
        QLearningAgent agent = new QLearningAgent();

        TrafficState state1 = new TrafficState(10, 20, 5, 15);
        TrafficState state2 = new TrafficState(10, 20, 5, 15);

        agent.setQValue(state1, TrafficAction.KEEP_PHASE, 0.8);

        assertEquals(0.8,
            agent.getQValue(state2, TrafficAction.KEEP_PHASE));
    }
    @Test
    void testIndependentActions() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(10, 20, 5, 15);

        agent.setQValue(state, TrafficAction.KEEP_PHASE, 0.8);

        assertEquals(0.8,
            agent.getQValue(state, TrafficAction.KEEP_PHASE));

        assertEquals(0.0,
            agent.getQValue(state, TrafficAction.REQUEST_PHASE_CHANGE));
    }
    @Test
    void testIndependentStates() {
        QLearningAgent agent = new QLearningAgent();

        TrafficState state1 = new TrafficState(10, 20, 5, 15);
        TrafficState state2 = new TrafficState(30, 10, 20, 5);

        agent.setQValue(state1, TrafficAction.KEEP_PHASE, 0.8);

        assertEquals(0.8,
            agent.getQValue(state1, TrafficAction.KEEP_PHASE));

        assertEquals(0.0,
            agent.getQValue(state2, TrafficAction.KEEP_PHASE));
    }
    @Test
    void testMaxQValue() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(10, 20, 5, 15);

        agent.setQValue(state, TrafficAction.KEEP_PHASE, 0.3);
        agent.setQValue(state, TrafficAction.REQUEST_PHASE_CHANGE, 0.9);

        assertEquals(0.9, agent.getMaxQValue(state));
    }
    @Test
    void testMaxNegativeQValue() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(10, 20, 5, 15);

        agent.setQValue(state, TrafficAction.KEEP_PHASE, -0.8);
        agent.setQValue(state, TrafficAction.REQUEST_PHASE_CHANGE, -0.3);

        assertEquals(-0.3, agent.getMaxQValue(state));
    }
    @Test
    void testMaxQValueForUnknownState() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(15, 25, 10, 20);

        assertEquals(0.0, agent.getMaxQValue(state));
    }
    @Test
    void testUpdateQValue() {
        QLearningAgent agent = new QLearningAgent();

        TrafficState state = new TrafficState(10, 20, 5, 15);
        TrafficState nextState = new TrafficState(8, 18, 4, 12);

        agent.setQValue(state, TrafficAction.KEEP_PHASE, 0.5);

        agent.setQValue(nextState, TrafficAction.KEEP_PHASE, 0.8);

        agent.updateQValue(
            state,
            TrafficAction.KEEP_PHASE,
            2.0,
            nextState,
            0.1,
            0.9
        );

        assertEquals(
            0.722,
            agent.getQValue(state, TrafficAction.KEEP_PHASE),
            0.0001
        );
    }
    @Test
    void testNegativeReward() {
        QLearningAgent agent = new QLearningAgent();

        TrafficState state = new TrafficState(10, 20, 5, 15);
        TrafficState nextState = new TrafficState(15, 25, 10, 20);

        agent.updateQValue(
            state,
            TrafficAction.KEEP_PHASE,
            -2.0,
            nextState,
            0.1,
            0.9
        );

        assertEquals(
            -0.2,
            agent.getQValue(state, TrafficAction.KEEP_PHASE),
            0.0001
        );
    }
    @Test
    void testProgressiveLearning() {
        QLearningAgent agent = new QLearningAgent();

        TrafficState state = new TrafficState(10, 20, 5, 15);
        TrafficState nextState = new TrafficState(8, 18, 4, 12);

        for (int i = 0; i < 100; i++) {
            agent.updateQValue(
                state,
                TrafficAction.KEEP_PHASE,
                2.0,
                nextState,
                0.1,
                0.9
            );
        }

        assertEquals(
            2.0,
            agent.getQValue(state, TrafficAction.KEEP_PHASE),
            0.001
        );
    }
    @Test
    void testInvalidAlpha() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(10, 20, 5, 15);

        assertThrows(IllegalArgumentException.class, () ->
            agent.updateQValue(
                state, TrafficAction.KEEP_PHASE,
                2.0, state, 1.5, 0.9
            )
        );
    }

    @Test
    void testInvalidGamma() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(10, 20, 5, 15);

        assertThrows(IllegalArgumentException.class, () ->
            agent.updateQValue(
                state, TrafficAction.KEEP_PHASE,
                2.0, state, 0.1, -0.5
            )
        );
    }
    @Test
    void testNaNReward() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(10, 20, 5, 15);

        assertThrows(IllegalArgumentException.class, () ->
            agent.updateQValue(
                state, TrafficAction.KEEP_PHASE,
                Double.NaN, state, 0.1, 0.9
            )
        );
    }

    @Test
    void testInfiniteReward() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(10, 20, 5, 15);

        assertThrows(IllegalArgumentException.class, () ->
            agent.updateQValue(
                state, TrafficAction.KEEP_PHASE,
                Double.POSITIVE_INFINITY, state, 0.1, 0.9
            )
        );
    }
    @Test
    void testBestAction() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(10, 20, 5, 15);

        agent.setQValue(state, TrafficAction.KEEP_PHASE, 0.3);
        agent.setQValue(state, TrafficAction.REQUEST_PHASE_CHANGE, 0.9);

        assertEquals(
            TrafficAction.REQUEST_PHASE_CHANGE,
            agent.getBestAction(state)
        );
    }
    @Test
    void testBestActionWhenEqual() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(10, 20, 5, 15);

        agent.setQValue(state, TrafficAction.KEEP_PHASE, 0.5);
        agent.setQValue(state, TrafficAction.REQUEST_PHASE_CHANGE, 0.5);

        assertEquals(
            TrafficAction.KEEP_PHASE,
            agent.getBestAction(state)
        );
    }
    @Test
    void testBestActionForUnknownState() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(5, 10, 2, 8);

        assertEquals(
            TrafficAction.KEEP_PHASE,
            agent.getBestAction(state)
        );
    }
    @Test
    void testBestActionKeepPhase() {
        QLearningAgent agent = new QLearningAgent();
        TrafficState state = new TrafficState(10, 20, 5, 15);

        agent.setQValue(state, TrafficAction.KEEP_PHASE, 0.9);
        agent.setQValue(state, TrafficAction.REQUEST_PHASE_CHANGE, 0.3);

        assertEquals(
            TrafficAction.KEEP_PHASE,
            agent.getBestAction(state)
        );
    }
}