package com.example.blackjack;

import java.util.ArrayList;
import java.util.Collections;

public class BlackjackGame {
    private Hand playerHand = new Hand();
    private Hand dealerHand = new Hand();
    private Deck deck = new Deck();

    public BlackjackGame() {
        dealCardToHand(playerHand);
        dealCardToHand(dealerHand);
        dealCardToHand(playerHand);
        dealCardToHand(dealerHand);
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public void dealCardToHand(Hand h) {
        h.addCard(deck.dealCard());
    }

    public void playerHit() {
        dealCardToHand(playerHand);
    }

    public boolean checkIfHandBust(Hand h) {
        if(calculateHandSum(h)>21) {
            return true;
        } else {
            return false;
        }
    }

    public boolean dealerDraw() {
        if (calculateHandSum(this.dealerHand) < 17) {
            dealCardToHand(this.dealerHand);
        } else {
            return false;
        }
        if (calculateHandSum(this.dealerHand) < 17) {
            return true;
        } else {
            return false;
        }
    }

    public Integer calculateHandSum(Hand h) {
        Integer cardSum = 0;
        boolean hasAce = false;
        for (Card c : h.getCards()) {
            cardSum += c.getValue();

            if (c.getValueString() == "A") {
                hasAce = true;
            }
        }
        if (hasAce == true) {
            cardSum +=10;
        }
        return cardSum;
    }

}

class Deck {
    private ArrayList<Card> cards = new ArrayList<>();


    public Deck() {

        // Initialize default deck
        this.cards.add(new Card(2,"2", "h"));
        this.cards.add(new Card(2,"2", "d"));
        this.cards.add(new Card(2,"2", "s"));
        this.cards.add(new Card(2,"2", "c"));
        this.cards.add(new Card(3,"3", "h"));
        this.cards.add(new Card(3,"3", "d"));
        this.cards.add(new Card(3,"3", "s"));
        this.cards.add(new Card(3,"3", "c"));
        this.cards.add(new Card(4,"4", "h"));
        this.cards.add(new Card(4,"4", "d"));
        this.cards.add(new Card(4,"4", "s"));
        this.cards.add(new Card(4,"4", "c"));
        this.cards.add(new Card(5,"5", "h"));
        this.cards.add(new Card(5,"5", "d"));
        this.cards.add(new Card(5,"5", "s"));
        this.cards.add(new Card(5,"5", "c"));
        this.cards.add(new Card(6,"6", "h"));
        this.cards.add(new Card(6,"6", "d"));
        this.cards.add(new Card(6,"6", "s"));
        this.cards.add(new Card(6,"6", "c"));
        this.cards.add(new Card(7,"7", "h"));
        this.cards.add(new Card(7,"7", "d"));
        this.cards.add(new Card(7,"7", "s"));
        this.cards.add(new Card(7,"7", "c"));
        this.cards.add(new Card(8,"8", "h"));
        this.cards.add(new Card(8,"8", "d"));
        this.cards.add(new Card(8,"8", "s"));
        this.cards.add(new Card(8,"8", "c"));
        this.cards.add(new Card(9,"9", "h"));
        this.cards.add(new Card(9,"9", "d"));
        this.cards.add(new Card(9,"9", "s"));
        this.cards.add(new Card(9,"9", "c"));
        this.cards.add(new Card(10,"10", "h"));
        this.cards.add(new Card(10,"10", "d"));
        this.cards.add(new Card(10,"10", "s"));
        this.cards.add(new Card(10,"10", "c"));
        this.cards.add(new Card(10,"J", "h"));
        this.cards.add(new Card(10,"J", "d"));
        this.cards.add(new Card(10,"J", "s"));
        this.cards.add(new Card(10,"J", "c"));
        this.cards.add(new Card(10,"Q", "h"));
        this.cards.add(new Card(10,"Q", "d"));
        this.cards.add(new Card(10,"Q", "s"));
        this.cards.add(new Card(10,"Q", "c"));
        this.cards.add(new Card(10,"K", "h"));
        this.cards.add(new Card(10,"K", "d"));
        this.cards.add(new Card(10,"K", "s"));
        this.cards.add(new Card(10,"K", "c"));
        this.cards.add(new Card(1,"A", "h"));
        this.cards.add(new Card(1,"A", "d"));
        this.cards.add(new Card(1,"A", "s"));
        this.cards.add(new Card(1,"A", "c"));

        // Shuffle deck
        Collections.shuffle(this.cards);
    }

    public Deck(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Card dealCard() {
        Card c = this.cards.get(0);
        this.cards.remove(0);
        return c;
    }
}

class Hand {
    private ArrayList<Card> cards = new ArrayList<>();

    public Hand() {}

    public Hand(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card c) {
        this.cards.add(c);
    }

    public void removeCard(int index) {
        this.cards.remove(index);
    }
}

class Card {
    private Integer value;
    private String valueString;
    private String suit;

    public Card(Integer value, String valueString, String suit) {
        this.value = value;
        this.valueString = valueString;
        this.suit = suit;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }
}