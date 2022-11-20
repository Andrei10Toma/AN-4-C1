#include <omp.h>
#include <stdio.h>
#include <stdlib.h>

#define N 20

int main(void) {
    int i;
    double coeficient = 5, sum = 0;
    double *arr = (double *)malloc(sizeof(double) * N);
    for (i = 0; i < N; i++) {
        arr[i] = (double) i;
    }

    # pragma omp parallel for private(i) shared(coeficient, arr) reduction(+:sum)
    for (i = 0; i < N; i++)
        sum += coeficient * arr[i];

    printf("%f\n", sum);
}
