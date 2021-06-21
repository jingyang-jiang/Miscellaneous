//
// Created by erres on 2020-03-06.
//


#include "blackjack.h"

#include <random>
#include <chrono>

using namespace std;

//Card methods
Card::Card(enum card_rank rank, enum card_type type):rank(rank),type(type){}
int Card::getValue() const {
    if(rank==JACK||rank==QUEEN||rank==KING)return 10;
    return rank;
}
void Card::displayCard (){
    char c;
    switch (type){
        case ClUBS:
            c='C';
            break;
        case DIAMONDS:
            c='D';
            break;
        case HEARTS:
            c='H';
            break;
        case SPADES:
            c='S';
            break;
        default:
            c='N';
    }
    switch (rank){
        case JACK:
            cout<<'J'<<c<<"   ";
            break;
        case QUEEN:
            cout<<'Q'<<c<<"   " ;
            break;
        case KING:
            cout<<'K'<<c<<"   ";
            break;
        default:
            cout<<rank<<c<<"   ";
    }
}


//Hand methods
void Hand::add(const Card &newCard){
    hand.push_back(newCard);
}
void Hand::clear(){
    hand.clear();
    hand.shrink_to_fit();
}
Hand::~Hand(){
    clear();
}
int Hand::getTotal() const {
    int total=0;
    int aceCount=0;
    //iterate through all cards in hand
    for(int i=0;i<hand.size();i++){
        //record num of Aces.
        if(hand[i].getValue()==1){
            aceCount++;
        }
        //calculate normally for now
            total = total + hand[i].getValue();
    }
    //add 10 to total for each Ace unless busted.
    while(aceCount!=0){
        if(total+10<=21)total=total+10;
        aceCount--;
    }
    return total;
}




//Deck methods
Deck::~Deck() {
hand.clear();
hand.shrink_to_fit();
}
void Deck::populate(){
    for(int i=ClUBS;i<=SPADES;i++){
        for(int j=ACE;j<=KING;j++){
            hand.emplace_back(static_cast<card_rank>(j), static_cast<card_type>(i));
        }
    }
}
void Deck::shuffle(){
    unsigned seed = std::chrono::system_clock::now().time_since_epoch().count();
    std::shuffle(hand.begin(),hand.end(), std::default_random_engine(seed));
}
void Deck::deal(Hand &playerHand){
    if(!hand.empty()){
        playerHand.add(hand.back());
        hand.pop_back();
        hand.shrink_to_fit();
    }
}


//AbstractPlayer methods
bool AbstractPlayer::isBusted(){
    return getTotal() > 21;
};


//HumanPlayer methods

bool HumanPlayer::isDrawing() const {
    char choice;
    cout<<"Do you want to draw? (y/n):"<<endl;
    cin>>choice;
    return choice == 'y';
}
void HumanPlayer::announce(int result ) {

switch(result){
    // 0 means player busted; 1 means casino busted; 2 player wins; 3 casino wins; 4 tie(push)
    case 0:
        cout<<"Player busts."<<endl;
        cout<<"Casino wins."<<endl;
        break;
    case 1:
        cout<<"Casino busts."<<endl;
        cout<<"Player wins."<<endl;
        break;
    case 2:
        cout<<"Player wins."<<endl;
        break;
    case 3:
        cout<<"Casino wins."<<endl;
        break;
    case 4:
        cout<<"Push: No one wins"<<endl;
        break;
    default:
        cout<<"Something went wrong"<<endl;

}
}


//ComputerPlayer methods
bool ComputerPlayer::isDrawing() const {
    return getTotal() <= 16;
}


//BlackJackGame methods


void BlackJackGame::play() {
    //initialize deck and casino
    m_deck.clear();
    m_deck.populate();
    m_deck.shuffle();
    m_casino.clear();
    //create player;
    HumanPlayer player;

    //deal cards to player and casino first
    m_deck.deal(player);
    m_deck.deal(player);
    m_deck.deal(m_casino);

    //display
    cout<<"Casino: ";
    m_casino.hand[0].displayCard();
    cout<<"["<<m_casino.getTotal()<<"]"<<endl;

    cout<<"Player: ";
    player.hand[0].displayCard();
    player.hand[1].displayCard();
    cout<<"["<<player.getTotal()<<"]"<<endl;
    //player draws
    while(player.isDrawing()){
        //deal one card
        m_deck.deal(player);
        //display all cards
        cout<<"Player: ";
        for(int i=0;i<player.hand.size();i++)player.hand[i].displayCard();
        cout<<"["<<player.getTotal()<<"]"<<endl;
        //check if busted
        if(player.isBusted()){
            player.announce(0);
            return;
        }
    }
    //computer draws
    while(m_casino.isDrawing()){
        //deal one card
        m_deck.deal(m_casino);
        //display all cards
        cout<<"Casino: ";
        for(int i=0;i<m_casino.hand.size();i++)m_casino.hand[i].displayCard();
        cout<<"["<<m_casino.getTotal()<<"]"<<endl;
        //check if busted
        if(m_casino.isBusted()){
            player.announce(1);
            return;
        }
    }
    // no one busts. compare
    if(player.getTotal()>m_casino.getTotal()){
        player.announce(2);
        return;
    }else if(player.getTotal()<m_casino.getTotal()){
        player.announce(3);
        return;
    }else{
        player.announce(4);
        return;
    }
}


