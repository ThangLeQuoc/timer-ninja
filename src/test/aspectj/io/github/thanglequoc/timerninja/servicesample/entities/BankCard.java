package io.github.thanglequoc.timerninja.servicesample.entities;

public class BankCard {
    private String cardNumber;
    private User cardHolder;
    private CardType cardType;

    public BankCard(String cardNumber, User cardHolder, CardType cardType) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.cardType = cardType;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public User getCardHolder() {
        return cardHolder;
    }
}
