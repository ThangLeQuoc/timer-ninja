package com.github.thanglequoc;

import com.github.thanglequoc.timerninja.Account;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");

        Account account = new Account();
        account.withdraw(5);
        System.out.println("Account balance: " + account.balance);
    }
}