package com.github.thanglequoc.timerninja.model;

import com.github.thanglequoc.timerninja.TimerNinjaTracker;

    /**
    * Dummy service class for testing purpose
    * */
    public class BankService {

        private PaymentService paymentService;

        @TimerNinjaTracker(enabled = false)
        public BankService() {
            CardService cardService = new CardService();
            try {
                Thread.sleep(90);
                paymentService = new PaymentService(cardService);

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
    }