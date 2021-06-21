#include<stdio.h>
#include<string.h>
#include<stdlib.h>
void load_and_convert(const char*filename){
        FILE *a=fopen(filename,"rt");
        if(a==NULL)exit(1);
        FILE *b=fopen("output.csv","wt");
        char name[1000];
        char age[1000];
        char address[1000];
        fgets(name,999,a);
        fgets(age,999,a);
        fgets(address,999,a);
        name[strlen(name)-1]='\0';
        age[strlen(age)-1]='\0';
        address[strlen(address)-1]='\0';
        const char s[2]=" ";
        char *ptr1,*ptr2,*ptr3;
        char *save1,*save2,*save3;
        ptr1=strtok_r(name,s,&save1);
        ptr2=strtok_r(age,s,&save2);
        ptr3=strtok_r(address,s,&save3);
        char record[100];
        while (ptr1 && ptr2 &&ptr3){
                strcat(record,ptr1);
                strcat(record,", ");
                strcat(record,ptr2);
                strcat(record,", ");
                strcat(record,ptr3);
                fputs(record,b);
                fputs("\n",b);
                memset(record,0,sizeof(record));
                ptr1=strtok_r(NULL,s,&save1);
                ptr2=strtok_r(NULL,s,&save2);
                ptr3=strtok_r(NULL,s,&save3);
        }
        fclose(a);
        fclose(b);
}
