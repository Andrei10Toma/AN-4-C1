// OpenMP header
#include <omp.h>
#include "../aux.h"

void main(int argc, char *argv[])
{
    if (argc < 2)  {
        printf("Usage: run_command N\n");
        exit(-1);
    }

    int N = atoi(argv[1]);
    int no_subsets = (int)pow(2,N);

    subset *allSubsets = malloc(no_subsets * sizeof(subset)); // final vector that will contain all the computed subsets

    for (int i = 0; i < no_subsets; i++)
    {
        allSubsets[i] = computeSubset(i, N);
    }

    printSubsets(allSubsets, no_subsets);
}