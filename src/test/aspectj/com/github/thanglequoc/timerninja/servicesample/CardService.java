package com.github.thanglequoc.timerninja.servicesample;

import com.github.thanglequoc.timerninja.TimerNinjaTracker;

/**
 * Dummy card service for testing purpose
 * */
public class CardService {
    public CardService() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @TimerNinjaTracker
    public boolean changeAmount(User user, int amount) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
