package com.github.thanglequoc.timerninja;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {
    private Account account;

    @BeforeEach
    public void before() {
        account = new Account();
    }

    @Test
    public void given20AndMin10_whenWithdraw10_thenFail() {
        account.withdrawA(100);
    }
}
