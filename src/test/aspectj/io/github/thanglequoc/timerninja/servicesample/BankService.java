package io.github.thanglequoc.timerninja.servicesample;

import io.github.thanglequoc.timerninja.TimerNinjaTracker;
import io.github.thanglequoc.timerninja.servicesample.entities.BankCard;
import io.github.thanglequoc.timerninja.servicesample.entities.BankRecordBook;
import io.github.thanglequoc.timerninja.servicesample.entities.User;
import io.github.thanglequoc.timerninja.servicesample.services.CardService;
import io.github.thanglequoc.timerninja.servicesample.services.NotificationService;
import io.github.thanglequoc.timerninja.servicesample.services.BalanceService;
import io.github.thanglequoc.timerninja.servicesample.services.UserService;

/**
* Dummy service class for testing purpose
* */
public class BankService {

    private BalanceService balanceService;
    private CardService cardService;
    private UserService userService;

    public BankService() {
        final BankRecordBook masterRecordBook = BankRecordBook.getInstance();
        NotificationService notificationService = new NotificationService();

        try {
            Thread.sleep(90);
            this.balanceService = new BalanceService(masterRecordBook, notificationService);
            this.userService = new UserService(masterRecordBook);
            this.cardService = new CardService(balanceService);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /* Test method to simulate transfer money */
    @TimerNinjaTracker(threshold = 200)
    public void requestMoneyTransfer(int sourceUserId, int targetUserId, int amount) {
        User sourceUser = userService.findUser(sourceUserId);
        User targetUser = userService.findUser(targetUserId);
        balanceService.deductAmount(sourceUser, amount);
        balanceService.increaseAmount(targetUser, amount);
    }

    /* Test method to simulate deposit money */
    @TimerNinjaTracker(includeArgs = true, threshold = 500)
    public void depositMoney(int userId, int amount) {
        depositMoney(userId, amount);
    }

    @TimerNinjaTracker(includeArgs = true)
    public void payWithCard(int userId, BankCard card, int amount) {
        User user = userService.findUser(userId);
        cardService.charge(user, card, amount);
    }

}