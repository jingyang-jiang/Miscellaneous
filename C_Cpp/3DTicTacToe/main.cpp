#include <iostream>
#include <string>
#include "functions.cpp"
using namespace std;



int main() {
    //Set up
    greatAndInstruct();
    string response;
    int invalid=1;
    do {
        getline(cin,response);
        if (response[0] == 'y' && response.length()==1) {
            invalid=0;
        } else if(response[0]=='n'&& response.length()==1){
            std::cout << "User chooses to exit." << std::endl;
            exit(0);
        } else{
            std::cout << "Input is invalid. Pls try again." <<std::endl;
        }
    }while(invalid);

    char board[28];
    int input;
    while(!checkWinner(board)){
        cout<<"Your Turn. Choose a valid cell."<<endl;
        cin>>input ;
        while(!checkIfLegal(input,board)){
            cout<<"Input is invalid. Pls try again."<<endl;
            cin>>input ;
        }
        board[input]='X';
        displayBoard(board);
        if(checkWinner(board)){
            cout<<"You have won! Program ends."<<endl;
            exit(0);
        }
        cout<<"Computer's turn"<<endl;
        computerMove(board);
        displayBoard(board);
        if(checkWinner(board)){
            cout<<"You lost. Program ends."<<endl;
            exit(0);
        }
    }

    return 0;

}
