#include<stdio.h>
#include<string.h>
#include<ctype.h>
int main(){
        char str1[100];
        char str2[100];
        int num1[26]={0},num2[26]={0},i=0;

        printf("Enter your first word: ");
        fgets(str1,99,stdin);
        str1[strlen(str1)-1]='\0';
        printf("Enter your second word: ");
        fgets(str2,99,stdin);
        str2[strlen(str2)-1]='\0';

        while(str1[i] != '\0'){
                num1[str1[i] - 'a']++;
                i++;
        }
        i=0;
        while(str2[i] != '\0'){
                num2[str2[i] - 'a']++;
                i++;
	}
        for (i=0;i<26;i++){
                if(num1[i] != num2[i])
                        return 1;
        }
        return 0;
}
