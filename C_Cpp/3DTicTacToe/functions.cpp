//
// Created by erres on 2020-02-11.
//
#include <iostream>
using namespace std;
void greatAndInstruct(){
    std::cout << "Hello and welcome to the Tic-Tac-Toe challenge: Player against Computer."<<std::endl;
    std::cout << "The board is numbered from 1 to 27 as per the following."<<std::endl;
    std::cout << " 1 | 2 | 3     10 | 11 | 12     19 | 20 | 21 " <<std::endl;
    std::cout << " ---------     ------------     ------------ " <<std::endl;
    std::cout << " 4 | 5 | 6     13 | 14 | 15     22 | 23 | 24 " <<std::endl;
    std::cout << " ---------     ------------     ------------ " <<std::endl;
    std::cout << " 7 | 8 | 9     16 | 17 | 18     25 | 26 | 27 " <<std::endl;
    std::cout << " Player starts first. Simply input the number of the cell you want to occupy." <<std::endl;
    std::cout << " Player's move is marked with X. Computer's move is marked with O." <<std::endl;
    std::cout << " Start?(y/n): " <<std::endl;
}
void displayBoard(char board[]){
    int i=1;
    while (i!=28){
        if(board [i] !='X' && board[i] != 'O'){
            cout << i << " | ";
        }else {
            cout << board[i] << " | ";
        }
        if(i%3==0){
            if(i==27){
                cout <<endl;
                return;
            } else if(i==24 || i==21){
                cout <<endl;
                cout << "-----------      --------------      -------------- " <<endl;
                i = i - 17;
            } else {
                cout << "     ";
                i = i + 7;
            }
        } else{
            i++;
        }
    }
}

bool checkIfLegal(int cellNbre, char board[]){
    if (cellNbre<1 || cellNbre >27)return false;
    return !(board[cellNbre] == 'X' || board[cellNbre] == 'O');
}

bool checkWinner(char board[]){
    for(int i=1;i<=27;i++){
        if(board[i] == 'X' || board[i] == 'O'){
            // first check win conditions that are achieved across tables
            if(i<10) {
                //same cell across three tables
                if (board[i] == board[i + 9] && board[i] == board[i + 18])return true;
                //diagonal across three tables
                if(i==1){
                        if(board[i] == board[i+13] && board[i] == board [i+26])return true;
                        if(board[i]==board[i+12] && board[i]==board[i+24])return true;
                }
                if (i==2){
                    if(board[i]==board[i+12] && board[i]==board[i+24])return true;
                }
                if (i==3){
                    if(board[i]==board[i+12] && board[i]==board[i+24])return true;
                    if(board[i]==board[i+11] && board[i]==board[i+22])return true;
                }
                if(i==7) {
                    if (board[i] == board[i + 7] && board[i] == board[i + 14])return true;
                    if (board[i] ==board[i+6] && board[i]==board[i+12])return true;
                }
                if (i==8){
                    if (board[i]==board[i+6] && board[i]==board[i+12])return true;
                }
                if (i==9){
                    if (board[i]==board[i+6] && board[i]==board[i+12])return true;
                    if (board[i]==board[i+5] && board[i]==board[i+10])return true;
                }
                //one line across three tables
                if (i%3==1){
                    if(board[i]==board[i+10] && board[i]==board[i+20])return true;
                }
                if (i%3==0){
                    if(board[i]==board[i+8] && board[i]==board[i+16])return true;
                }
            }
            //Now check win conditions within one table
            //check row within the same table
            if(i%9==1 || i%9==4 || i%9==7 ){
                if(board[i]==board[i+1] && board[i]==board[i+2])return true;
            }
            //check col within the same table
            if(i%9==1 || i%9==2 || i%9==3 ){
                if(board[i]==board[i+3] && board[i]==board[i+6])return true;
            }
            //check diagonal within the same table
            if(i%9==1){
                if(board[i]==board[i+4] && board[i]==board[i+8])return true;
            }
            if(i%9 ==3){
                if(board[i]==board[i+2] && board[i]==board[i+4])return true;
            }

        }
    }
    return false;
}

void computerMove(char board[]){
    //check all cells for possible win
    for(int i=1;i<=27;i++){
        if(checkIfLegal(i,board)){
            char null=board[i];
            board[i]='O';
            if(checkWinner(board)){
                return;
            }
            board[i]=null;
        }
    }
    //check all cells for possible loss
    for(int i=1;i<=27;i++){
        if(checkIfLegal(i,board)){
            char null=board[i];
            board[i]='X';
            if(checkWinner(board)){
                board[i]='O';
                return;
            }
            board[i]=null;
        }
    }
    //occupy center if possible
    for(int i=5;i<27;i=i+9){
        if(checkIfLegal(i,board)){
            board[i]='O';
            return;
        }
    }
    //pick random cell
    int randomMove=1+(rand()%27);
    while(!checkIfLegal(randomMove,board)){
        randomMove=1+(rand()%27);
    }
    board[randomMove]='O';
    return;
}
