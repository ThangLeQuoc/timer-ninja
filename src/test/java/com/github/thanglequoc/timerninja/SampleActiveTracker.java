package com.github.thanglequoc.timerninja;

import java.util.Random;

public class SampleActiveTracker {

    @TimerNinjaTracker(enabled = true)
    public Integer getAccountNumber(String userName, int id) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Random().nextInt();
    }

    @TimerNinjaTracker(enabled = true)
    public void doTransferAccount(int currentAccountNumber, String targetAccountNumber, Bank bank) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
