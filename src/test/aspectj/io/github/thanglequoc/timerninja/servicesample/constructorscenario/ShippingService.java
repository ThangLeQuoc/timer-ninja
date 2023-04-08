package io.github.thanglequoc.timerninja.servicesample.constructorscenario;

import io.github.thanglequoc.timerninja.TimerNinjaTracker;

public class ShippingService {

    private LocationService locationService;

    @TimerNinjaTracker
    public ShippingService() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.locationService = new LocationService();
    }
}
