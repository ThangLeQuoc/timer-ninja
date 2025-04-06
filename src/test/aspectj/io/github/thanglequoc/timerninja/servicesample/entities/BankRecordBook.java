package io.github.thanglequoc.timerninja.servicesample.entities;

import io.github.thanglequoc.timerninja.TimerNinjaTracker;

import java.util.HashMap;
import java.util.Map;

/**
 * The bank record book, keeping all user record and information, act as a database
 * */
public class BankRecordBook {

    private static BankRecordBook INSTANCE;

    private static final Map<User, Integer> userBalance = new HashMap<>();

    private BankRecordBook() {
        initBankRecordBook();
    }

    public static BankRecordBook getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BankRecordBook();
        }
        return BankRecordBook.INSTANCE;
    }

    @TimerNinjaTracker
    /* Initialize the bank record book with some dummy users */
    private void initBankRecordBook() {
        User user1 = new User(1, "user-1", "usr01@timerninja.io");
        User user2 = new User(2, "user-2", "usr02@timerninja.io");
        User user3 = new User(3, "user-3", "usr03@timerninja.io");

        userBalance.put(user1, 55000);
        userBalance.put(user2, 15000);
        userBalance.put(user3, 25000);
    }

    @TimerNinjaTracker
    public Map<User, Integer> getUserBalance() {
        return userBalance;
    }
}
