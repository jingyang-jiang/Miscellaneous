#include<stdio.h>
#include<stdlib.h>
void read_csv(const char *csv_filename){
        char c;
        FILE *p=fopen(csv_filename,"rt");
        if(p==NULL)exit(1);
        c=fgetc(p);
        while(!feof(p)){
                printf("%c",c);
                c=fgetc(p);
        }
        printf("\n");
        fclose(p);
}
