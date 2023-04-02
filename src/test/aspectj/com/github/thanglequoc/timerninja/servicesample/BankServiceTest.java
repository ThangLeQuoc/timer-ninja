package com.github.thanglequoc.timerninja.servicesample;

import org.junit.jupiter.api.Test;

public class BankServiceTest {

    @Test
    public void test() {
        BankService bankService = new BankService();
        BankService.getBankName();
        bankService.getBankNumber(new User(12, "user-12"));
    }

    @Test
    public void testConstructorLog() {
        BankService bankService = new BankService();
    }

    @Test
    public void testOrdering() {
//        BankService bankService = new BankService();
//        bankService.testPaymentServiceConnections();
    }
}
