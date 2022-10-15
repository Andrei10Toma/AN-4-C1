#include "matrix.h"

int main() {
    int i, j, k;
    initializeMatrix();

    for (i = 0; i < n; i ++) {
        for (j = 0; j < n; j++) {
            c[i][j] = 0;
            for(k = 0; k < n; k++) {
                c[i][j] += a[i][k] * b[k][j];
            }
        }
    }

    printMatrixes();
}