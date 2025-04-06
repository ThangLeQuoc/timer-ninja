package io.github.thanglequoc.timerninja.servicesample.services;

import java.time.temporal.ChronoUnit;

import io.github.thanglequoc.timerninja.TimerNinjaTracker;
import io.github.thanglequoc.timerninja.servicesample.entities.BankRecordBook;
import io.github.thanglequoc.timerninja.servicesample.entities.User;

public class UserService {

    private final BankRecordBook recordBook;

    public UserService(BankRecordBook recordBook) {
        try {
            Thread.sleep(100);
            this.recordBook = recordBook;
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
        return recordBook.getUserBalance().keySet().stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
    }
}
