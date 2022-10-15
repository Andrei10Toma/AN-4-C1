// OpenMP header
#include <omp.h>
#include <stdio.h>
#include <stdlib.h>

#define N 10000

void main()
{
    int sum=0;
    int lsum=0;
    int *A = malloc(N * sizeof(int));
    for (int i = 0; i < N; i++) {
        A[i] = i;
    }

    #pragma omp parallel private(lsum)
    {
        int i;
        #pragma omp for
            for (i=0; i<N; i++)
            {
                lsum = lsum +A[i];
            }
        #pragma omp critical
            {
                sum+=lsum;
            }
    }
    printf("Vector sum is %d\n", sum);

}