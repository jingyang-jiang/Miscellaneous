#include <iostream>
#include "blackjack.h"
int main() {

    std::cout << "\tWelcome to the Comp322 Blackjack game!" << std::endl << std::endl;
    BlackJackGame game;
    //The main loop of the game
    bool playAgain = true;
    char answer = 'y';
    while (playAgain)
    {
        game.play();
    //Check whether the player would like to play another round
        std::cout << "Would you like another round? (y/n): ";
        std::cin >> answer;
        std::cout << std::endl << std::endl;
        playAgain = (answer == 'y' ? true : false);
    }
    std::cout <<"Gave over!";
    return 0;

}
