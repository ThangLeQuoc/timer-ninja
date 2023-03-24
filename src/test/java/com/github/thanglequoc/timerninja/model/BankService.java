package com.github.thanglequoc.timerninja.model;

import com.github.thanglequoc.timerninja.Bank;
import com.github.thanglequoc.timerninja.TimerNinjaTracker;

/**
     * Dummy service class for testing purpose
     * */
    public class BankService {

        private PaymentService paymentService;

        @TimerNinjaTracker(enabled = false)
        public BankService() {
            try {
                Thread.sleep(90);
                paymentService = new PaymentService();
                paymentService.warmUp();
                paymentService.warmUp2();
                paymentService.warmUp3();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    
        @TimerNinjaTracker
        String getBankNumber(User user) {
            return "abc";
        }
    
        @TimerNinjaTracker
        public static final String getBankName() {
            connectingToExternalNamingService();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "tle bank";
        }

        @TimerNinjaTracker
        private static void connectingToExternalNamingService() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @TimerNinjaTracker
        public void testPaymentServiceConnections() {
            paymentService.warmUp();
            paymentService.warmUp2();
            paymentService.warmUp3();
        }
    }