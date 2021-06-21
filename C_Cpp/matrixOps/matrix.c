#include<stdlib.h>
#include<stdio.h>
#include<time.h>
const int rows=5;
const int cols=5;

void fillMatrix(int matrix[rows][cols]){
	srand(time(0));
        for(int i=0;i<cols;i++){
                for(int j=0;j<rows;j++){
                        matrix[i][j]=rand() % 100 + 1;
                }
        }

}
  
void printMatrix(int matrix[rows][cols]){
        for(int i=0;i<cols;i++){
                for(int j=0;j<rows;j++){
                        printf("%d ",matrix[i][j]);
                }
                printf("\n");
        }
	printf("\n");
}

void transposeMatrix(int matrix[rows][cols]){
        int *ptr=matrix;
        for(int i=0;i<(rows-1);i++){
             for(int j=(i+1);j<cols;j++){
                int temp=*(ptr+i*cols+j);
                *(ptr+i*cols+j)=*(ptr+j*cols+i);
                *(ptr+j*cols+i)=temp;
             }
     }
}

void multiplyMatrix(int m1[2][cols],int m2[rows][cols],int resultMatrix[rows][cols]){
        int *ptrResult=resultMatrix;
        int *ptrm1=m1;
        int *ptrm2=m2;
        int multsum;
        for(int i=0;i<2;i++){
                for(int j=0;j<5;j++){
                        multsum=0;
                        for(int k=0;k<5;k++){
                                multsum +=*((ptrm1+i*cols+k))* (*(ptrm2+j+k*cols));
                        }
                        *ptrResult=multsum;
                        ptrResult++;
        }
        }
        for(int i=0;i<15;i++){
                *ptrResult=0;
                ptrResult++;
        }
        
}
int main(){
        int matrix[rows][cols];
        fillMatrix(matrix);
        printMatrix(matrix);
        transposeMatrix(matrix);
        printMatrix(matrix);
        int matrix2[2][cols]={0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int matrixResult[rows][cols];
        multiplyMatrix(matrix2,matrix,matrixResult);
        printMatrix(matrixResult);
        return 0;
}
