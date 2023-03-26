package timerninja.model;

import com.github.thanglequoc.timerninja.TimerNinjaTracker;

public class PaymentService {

    @TimerNinjaTracker
    public PaymentService() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @TimerNinjaTracker
    public void warmUp() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @TimerNinjaTracker
    public void warmUp2() {}

    @TimerNinjaTracker
    public void warmUp3() {}
}
