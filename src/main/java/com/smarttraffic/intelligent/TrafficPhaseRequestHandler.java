
package com.smarttraffic.intelligent;

public interface TrafficPhaseRequestHandler {

    void requestPhase(String phase);

    String getCurrentPhase();
}
