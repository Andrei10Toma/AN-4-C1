// OpenMP header
#include <omp.h>
#include "../aux.h"

void main(int argc, char *argv[])
{
    //parse command line arguments
    if (argc < 3)  {
        printf("Usage: run_command N P\n");
        exit(-1);
    }

    int N = atoi(argv[1]);
    int P = atoi(argv[2]);
    omp_set_num_threads(P);
    int no_subsets = (int)pow(2,N);
    if (no_subsets % P != 0) {
        printf("Current number of threads is NOT a divisor of 2^N!\n");
        exit(-1);
    }

    subset *threadSubsets; // vector for every thread to put the computed subsets in
    subset *allSubsets = malloc(no_subsets * sizeof(subset)); // final vector that will contain all the computed subsets
    int allSubsetsLen = 0;

    #pragma omp parallel private(threadSubsets) shared(no_subsets, P, allSubsets, allSubsetsLen)
    {
        int len = 0;
        threadSubsets = malloc((no_subsets / P) * sizeof(subset));
        #pragma omp for
            // compute subset for every rank
            for (int i = 0; i < no_subsets; i++)
            {
                threadSubsets[len] = computeSubset(i, N);
                len += 1;
            }
        #pragma omp critical
            { // gather all computed subsets in the final vector of subsets(allSUbsets)
                for(int i = 0; i < len; i++) {
                    allSubsets[allSubsetsLen++] = threadSubsets[i];
                }
            }
    }

    printSubsets(allSubsets, allSubsetsLen);
}