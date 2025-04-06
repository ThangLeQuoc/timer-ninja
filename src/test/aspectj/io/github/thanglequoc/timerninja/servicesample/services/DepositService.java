package io.github.thanglequoc.timerninja.servicesample.services;

import io.github.thanglequoc.timerninja.servicesample.entities.BankRecordBook;
import io.github.thanglequoc.timerninja.servicesample.entities.User;

public class DepositService {

    private BankRecordBook recordBook;
    private NotificationService notificationService;

    public DepositService(BankRecordBook recordBook, NotificationService notificationService) {
        this.recordBook = recordBook;
        this.notificationService = notificationService;
    }

    public void depositMoney(User user, int amount) {
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        recordBook.getUserBalance().compute(user, (k, currentBalance) -> currentBalance + amount);
        notificationService.notify(user);
    }
}
