package com.github.thanglequoc.timerninja.model;

import com.github.thanglequoc.timerninja.TimerNinjaTracker;

public class PaymentService {

    private CardService cardService;
    private NotificationService notificationService;

    @TimerNinjaTracker
    public PaymentService(CardService cardService) {
        this.cardService = cardService;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void processPayment(User user, int amount) {
        cardService.deductAmount(user, amount);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        notificationService.notify(user);
    }


}
