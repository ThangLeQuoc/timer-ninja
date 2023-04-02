package com.github.thanglequoc.timerninja.servicesample;

import java.time.temporal.ChronoUnit;

import com.github.thanglequoc.timerninja.TimerNinjaTracker;

public class UserService {

    @TimerNinjaTracker
    public UserService() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @TimerNinjaTracker(timeUnit = ChronoUnit.MICROS)
    public User findUser(int userId) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        User user = new User(userId, "user-"+userId);
        return user;
    }
}
