package com.github.thanglequoc.timerninja.servicesample;

import com.github.thanglequoc.timerninja.TimerNinjaTracker;

    /**
    * Dummy service class for testing purpose
    * */
    public class BankService {

        private PaymentService paymentService;
        private UserService userService;

        public BankService() {
            CardService cardService = new CardService();
            NotificationService notificationService = new NotificationService();
            try {
                Thread.sleep(90);
                this.paymentService = new PaymentService(cardService, notificationService);
                this.userService = new UserService();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    
        @TimerNinjaTracker
        public String getBankNumber(int userId) {
            User user = userService.findUser(userId);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "BankAccountNo-" + user.getId();
        }

        @TimerNinjaTracker
        public void requestMoneyTransfer(int sourceUserId, int targetUserId, int amount) {
            User sourceUser = userService.findUser(sourceUserId);
            User targetUser = userService.findUser(targetUserId);
            paymentService.processPayment(sourceUser, amount);
            paymentService.processPayment(sourceUser, -amount);
        }

    }