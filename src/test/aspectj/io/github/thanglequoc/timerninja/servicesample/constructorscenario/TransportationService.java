package io.github.thanglequoc.timerninja.servicesample.constructorscenario;

import io.github.thanglequoc.timerninja.TimerNinjaTracker;

public class TransportationService {

    private ShippingService shippingService;

    @TimerNinjaTracker
    public TransportationService() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.shippingService = new ShippingService();
    }
}
