#include<stdlib.h>
#include<string.h>
#include<stdio.h>
#include<stddef.h>
int main(){
        char *data;
        data=getenv("QUERY_STRING");
        //char *data="number=16";
        char buffer[256];
        char input[100];
        int n;
        strncpy(buffer,data,255);
        sscanf(buffer,"number=%s",input);
        sscanf(input,"%d",&n);
        int binary[32];
        int i=0;
        while(n>0){
                binary[i]=n%2;
                n=n/2;
                i++;
        }
        printf("Content-Type:text/html\n\n");
        printf("<html>");
        for(int j=i-1;j>=0;j--){
                printf("%d",binary[j]);

        }
        printf("</html>");
        return 0;

}
