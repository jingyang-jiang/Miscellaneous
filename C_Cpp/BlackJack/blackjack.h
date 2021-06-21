//
// Created by erres on 2020-03-06.
//

#ifndef COMP322_ASSIGNMENT2_BLACKJACK_H
#define COMP322_ASSIGNMENT2_BLACKJACK_H
#include <vector>
#include <iostream>
#include <algorithm>

    enum card_rank {ACE = 1, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING};
    enum card_type {ClUBS, DIAMONDS, HEARTS, SPADES};
class Card{
public:
    Card(enum card_rank rank, enum card_type type);
    int getValue() const;
    void displayCard();
private:
    enum card_rank rank;
    enum card_type type;
};
class Hand{
public:
    virtual ~Hand();
    void add(const Card &newCard);
    void clear();
    int getTotal() const;

public:
    std::vector<Card> hand;
};
class Deck:public Hand{
public:
    ~Deck() override;
    void populate();
    void shuffle();
    void deal(Hand &player);

};

class AbstractPlayer : public Hand{
public:
    virtual bool isDrawing() const=0;
    bool isBusted();
};


class HumanPlayer:public AbstractPlayer{
public:
    bool  isDrawing() const override;
    void announce(int result);
};



class ComputerPlayer:public AbstractPlayer{
public:
    bool  isDrawing() const override;

};




class BlackJackGame{
public:


    void play();

private:
    Deck m_deck;
    ComputerPlayer m_casino;


};
#endif //COMP322_ASSIGNMENT2_BLACKJACK_H
