package com.github.thanglequoc.timerninja;

import com.github.thanglequoc.timerninja.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountTest {
    private Account account;

    @BeforeEach
    public void before() {
        account = new Account();
    }

    @Test
    public void given20AndMin10_whenWithdraw5_thenSuccess() {
        assertTrue(account.withdraw(5));
    }

    @Test
    public void given20AndMin10_whenWithdraw100_thenFail() {
        assertFalse(account.withdraw(100));
    }

    @Test
    public void given20AndMin10_whenWithdraw10_thenFail() {
        account.withdrawA(100);
    }
}
