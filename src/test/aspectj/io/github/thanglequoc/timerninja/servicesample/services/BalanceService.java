package io.github.thanglequoc.timerninja.servicesample.services;

import io.github.thanglequoc.timerninja.TimerNinjaTracker;
import io.github.thanglequoc.timerninja.servicesample.entities.BankRecordBook;
import io.github.thanglequoc.timerninja.servicesample.entities.User;

public class BalanceService {

    private final BankRecordBook recordBook;
    private final NotificationService notificationService;

    public BalanceService(BankRecordBook recordBook, NotificationService notificationService) {
        this.recordBook = recordBook;
        this.notificationService = notificationService;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int checkCurrentBalance(User user) {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return recordBook.getUserBalance().get(user);
    }

    @TimerNinjaTracker(includeArgs = true)
    public void increaseAmount(User user, int amount) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        recordBook.getUserBalance().compute(user, (u, balance) -> balance + amount);
        notificationService.notify(user);
    }

    /*
    * Integration test note: The threshold setting must met
    *  */
    @TimerNinjaTracker(includeArgs = true, threshold = 500)
    public void deductAmount(User user, int amount) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int currentBalance = recordBook.getUserBalance().get(user);
        if (amount > currentBalance) {
            throw new RuntimeException("Insufficient balance");
        } else {
            recordBook.getUserBalance().compute(user, (u, balance) -> balance - amount);
        }
        notificationService.notify(user);
    }
}
