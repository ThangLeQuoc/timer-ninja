package com.github.thanglequoc.timerninja.model;

import org.junit.jupiter.api.Test;

public class BankServiceTest {

    @Test
    public void test() {
        BankService bankService = new BankService();
        BankService.getBankName();
        bankService.getBankNumber(new User());
    }
}
