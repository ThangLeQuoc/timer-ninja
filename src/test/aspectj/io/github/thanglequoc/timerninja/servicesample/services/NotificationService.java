package io.github.thanglequoc.timerninja.servicesample.services;

import io.github.thanglequoc.timerninja.TimerNinjaTracker;
import io.github.thanglequoc.timerninja.servicesample.entities.User;

public class NotificationService {
    public NotificationService() {
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @TimerNinjaTracker
    public void notify(User user) {
        notifyViaSMS(user);
        notifyViaEmail(user);
    }

    @TimerNinjaTracker
    private void notifyViaSMS(User user) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @TimerNinjaTracker
    private void notifyViaEmail(User user) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
