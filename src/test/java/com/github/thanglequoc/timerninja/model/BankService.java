package com.github.thanglequoc.timerninja.model;

import com.github.thanglequoc.timerninja.TimerNinjaTracker;

/**
     * Dummy service class for testing purpose
     * */
    public class BankService {
    
        @TimerNinjaTracker
        String getBankNumber(User user) {
            return "abc";
        }
    
        @TimerNinjaTracker
        public static final String getBankName() {
            return "tle bank";
        }
    }