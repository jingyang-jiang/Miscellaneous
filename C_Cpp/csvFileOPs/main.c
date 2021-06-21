#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include "convert_to_csv.h"
#include "read_csv.h"

void find_name(const char *csv_filename, const char*name){
        FILE *p=fopen(csv_filename,"rt");
        if(p==NULL)exit(1);
        char record[1000];
        int find=0;
        fgets(record,999,p);
        while(!feof(p)){
                if(strstr(record,name)!=NULL){
                        printf("%s",record);
                        find++;
                }
                fgets(record,999,p);
                if(feof(p)&&find==0){
                        printf("Name %s not found\n",name);
                }
        }
        fclose(p);
        printf("\n");
}
void add_record(const char*csv_filename, const char*name, const int age, const char*city){
        FILE *p=fopen(csv_filename,"a");
        fprintf(p,"%s, %d, %s\n",name,age,city);
        fclose(p);
}
void delete_record(const char *csv_filename, const char*name){
        FILE *a,*b;
        char c;
        int delete_line=1, temp = 1,find=0;
        a=fopen(csv_filename,"r");
        if(a==NULL)exit(1);
        //find the line number to be deleted
        char record[1000];
        fgets(record,999,a);
        while(!feof(a)){
                if(strstr(record,name)!=NULL){
                        find++;
                        break;
                }
                fgets(record,999,a);
                delete_line++;
                if(feof(a)&&find==0){
                        printf("No such record to be deleted.");
                        fclose(a);
                        return;
                }
        }
        //copy except the deleted line
        rewind(a);
        b=fopen("replica.csv","w");
        c=getc(a);
        while(!feof(a)){
                if(temp!=delete_line){
                        putc(c,b);
                }
                if(c=='\n'){
                        temp++;
                }
                c=getc(a);
        }
        fclose(a);
        fclose(b);
        remove(csv_filename);
        rename("replica.csv",csv_filename);


}

int main(){
        load_and_convert("input.txt");
        read_csv("output.csv");
        find_name("output.csv","Maria");
        find_name("output.csv","Jason");
        add_record("output.csv","Jason",36,"Skookumchuk");
        read_csv("output.csv");
        delete_record("output.csv","Maria");
        read_csv("output.csv");
}
