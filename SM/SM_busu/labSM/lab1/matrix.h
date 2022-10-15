#include <stdio.h>
#define n 16

int a[n][n];
int b[n][n];
int c[n][n];
int p[n][n][n];
int v[n][n][n];

struct parrent
{
    int i;
    int j;
    int k;
};

void initializeMatrix() {
    for (int i = 0; i < n; i++) {
        for(int j = 0; j < n; j++) {
            a[i][j] = j + 10;
            b[i][j] = i + 10;
        }
    }
}

void printMatrixes() {
    printf("A\n");
    for(int i = 0; i < n; i++) {
        for(int j = 0; j < n; j++) {
            printf("%d ", a[i][j]);
        }
        printf("\n");
    }

    printf("B\n");
    for(int i = 0; i < n; i++) {
        for(int j = 0; j < n; j++) {
            printf("%d ", b[i][j]);
        }
        printf("\n");
    }

    printf("A*B\n");
    for(int i = 0; i < n; i++) {
        for(int j = 0; j < n; j++) {
            printf("%d ", c[i][j]);
        }
        printf("\n");
    }
}