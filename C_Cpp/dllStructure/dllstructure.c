#include<string.h>
#include<stdlib.h>
#include<stdio.h>
typedef struct NODE {
        int data;
        struct NODE *next;
        struct NODE *previous;
} Node ;


Node* create_dll_from_array(int[],int);
void print_dll(Node*);
void insert_after(Node*,int, int);
void delete_element(Node*,int);
void sort_dll(Node*);
void freeall(Node*);

int main(){
        int array[5]={11,2,7,22,4};
        Node*head;
        //Q1
        head = create_dll_from_array(array,5);

        //Q2
        print_dll(head);

        //Q3
        insert_after(head,7,13);
        insert_after(head,21,29);
        print_dll(head);

        //Q4
        delete_element(head,22);
        print_dll(head);
        delete_element(head,11);
        print_dll(head);

        //Q5
        sort_dll(head);
        print_dll(head);

        //Q6
        freeall(head);
        return 0;
}
Node* create_dll_from_array(int array[],int size){
        Node *head,*new,*temp;
        int i;
        for(i=0;i<size;i++){
                new =(Node*)malloc(sizeof(Node));
                new->data=array[i];
                if(i==0){
                        head=new;
                        new->previous=head;
                        new->next=head;

                }else{
                        temp=head->previous;
                        temp->next=new;
                        new->next=head;
                        new->previous=temp;
                        temp=head;
                        temp->previous=new;
                }
        }
        head->previous->next=NULL;
        head->previous=NULL;
        return head;

}
void print_dll(Node *head){
        Node *temp=head;
        while (temp != NULL){
                printf("%d  " ,temp->data);
                temp=temp->next;
        }
        printf("\n");
}

void insert_after(Node *head, int valueToInsertAfter, int valueToBeInserted){
        Node *current=head;
        Node *new=(Node*)malloc(sizeof(Node));
        new->data=valueToBeInserted;
        while(current->data != valueToInsertAfter && current->next !=NULL){
                current=current->next;
        }
        if(current->next==NULL){
                current->next=new;
                new->previous=current;
                new->next=NULL;
        }else{
                current->next->previous=new;
                new->next=current->next;
                new->previous=current;
                current->next=new;
        }

}
void delete_element(Node*head,int valueToBeDeleted){
        Node*temp=head;
        while(temp!=NULL){
                if(temp->data==valueToBeDeleted&&temp->previous==NULL){
                        temp->data=temp->next->data;
                        temp=temp->next;
                        temp->next->previous=head;
                        head->next=temp->next;
                        free(temp);
                        return;
                }
                if(temp->data==valueToBeDeleted&&temp->previous!=NULL&&temp->next!=NULL){
                        temp->next->previous=temp->previous;
                        temp->previous->next=temp->next;
                        free(temp);
                        return;
                }
                if(temp->data==valueToBeDeleted&&temp->previous!=NULL&&temp->next==NULL){
                        temp->previous->next=NULL;
                        free(temp);
                        return;
                }
                temp=temp->next;
        }
        if(temp==NULL){
                return;
        }
}
void sort_dll(Node* head){
        int swapped=0;
        Node* ptr1=head;
        Node* ptr2=head->next;
        while(ptr2->next!=NULL){
                if(ptr1->data > ptr2->data){
                        int temp=ptr1->data;
                        ptr1->data=ptr2->data;
                        ptr2->data=temp;
                        swapped++;
                }
                ptr2=ptr2->next;
                ptr1=ptr1->next;
        }
        while(swapped!=0){
                swapped=0;
                Node* ptr1=head;
                Node* ptr2=head->next;
                while(ptr2->next!=NULL){
                        if(ptr1->data > ptr2->data){
                        int temp=ptr1->data;
                        ptr1->data=ptr2->data;
                        ptr2->data=temp;
                        swapped++;
                        }
                ptr2=ptr2->next;
                ptr1=ptr1->next;
                }
        }
}

void freeall(Node*head){
        Node *temp=head;
        while(temp->next!=NULL){
                temp=temp->next;
        }
        while(temp->previous!=NULL){
                temp=temp->previous;
                free(temp->next);
        }
        free(temp);

}
