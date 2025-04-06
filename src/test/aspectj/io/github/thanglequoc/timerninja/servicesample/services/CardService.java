package io.github.thanglequoc.timerninja.servicesample.services;

import io.github.thanglequoc.timerninja.TimerNinjaTracker;
import io.github.thanglequoc.timerninja.servicesample.entities.BankCard;
import io.github.thanglequoc.timerninja.servicesample.entities.CardType;
import io.github.thanglequoc.timerninja.servicesample.entities.User;

/**
 * Dummy card service for testing purpose
 * */
public class CardService {

    private final BalanceService balanceService;

    public CardService(BalanceService balanceService) {
        try {
            Thread.sleep(150);
            this.balanceService = balanceService;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @TimerNinjaTracker(includeArgs = true, threshold = 500)
    public void charge(User user, BankCard card, int amount) {
        // Simulate getting the information from card provider
        try {
            if (card.getCardType() == CardType.VISA) {
                Thread.sleep(200);
            } else if (card.getCardType() == CardType.MASTER_CARD) {
                Thread.sleep(500);
            } else if (card.getCardType() == CardType.AMERICAN_EXPRESS) {
                Thread.sleep(2000);
            }
            else if (card.getCardType() == CardType.JCB) { // simulate the connection issue with JCB card
                throw new IllegalStateException("Unable to acquire connection to JCB");
            } else {
                throw new IllegalStateException("Unable to acquire connection to the card provider");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        balanceService.deductAmount(user, amount);
    }
}
