
package com.smarttraffic.intelligent;

public class TestAdaptiveTraffic {

    public static void main(String[] args) {

        TrafficSensor nord = new TrafficSensor("NORD");
        TrafficSensor sud = new TrafficSensor("SUD");
        TrafficSensor est = new TrafficSensor("EST");
        TrafficSensor ouest = new TrafficSensor("OUEST");

        nord.updateVehicleCount(25);
        sud.updateVehicleCount(20);
        est.updateVehicleCount(3);
        ouest.updateVehicleCount(2);

        AdaptiveTrafficController controller =
            new AdaptiveTrafficController();

        TrafficSensor[] sensors = {
            nord, sud, est, ouest
        };

        System.out.println("=== SMARTTRAFFIC ===");
        System.out.println("Simulation des feux intelligents\n");

        for (TrafficSensor sensor : sensors) {

            int greenDuration =
                controller.calculateGreenDuration(sensor);

            String congestion =
                controller.analyzeTraffic(sensor, 30);

            System.out.println(
                "Direction : " + sensor.getLaneId()
            );

            System.out.println(
                "Vehicules : " + sensor.getVehicleCount()
            );

            System.out.println(
                "Congestion : " + congestion
            );

            System.out.println(
                "Duree du vert : " + greenDuration + " s"
            );

            System.out.println("----------------------");
        }
    }
}
