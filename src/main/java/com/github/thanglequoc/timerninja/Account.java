package com.github.thanglequoc.timerninja;

import java.time.temporal.ChronoUnit;

public class Account {
    public int balance = 20;


    public boolean withdraw(int amount) {
        if (balance < amount) {
            return false;
        }
        balance = balance - amount;
        return true;
    }

    @TimerNinjaTracker(enabled = true, timeUnit = ChronoUnit.MILLIS)
    public void withdrawA(int amount) {
        balance = balance - amount;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
