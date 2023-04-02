package com.github.thanglequoc.timerninja.servicesample;

/**
 * Dummy card service for testing purpose
 * */
public class CardService {

    public boolean deductAmount(User user, int amount) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
