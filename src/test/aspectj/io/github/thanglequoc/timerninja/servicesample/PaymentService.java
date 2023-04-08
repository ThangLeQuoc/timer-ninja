package io.github.thanglequoc.timerninja.servicesample;

import io.github.thanglequoc.timerninja.TimerNinjaTracker;

public class PaymentService {

    private CardService cardService;
    private NotificationService notificationService;

    public PaymentService(CardService cardService, NotificationService notificationService) {
        this.cardService = cardService;
        this.notificationService = notificationService;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @TimerNinjaTracker
    public void processPayment(User user, int amount) {
        cardService.changeAmount(user, amount);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        notificationService.notify(user);
    }
}
