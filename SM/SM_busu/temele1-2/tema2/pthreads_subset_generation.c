#include "../aux.h"
#include <pthread.h>


subset *allSubsets;
int noSubsets;
int P;
int N;

void *computeSubsetsPerThread(void *arg) {
    int id = *(int *)arg;

    //compute start and end ranks for each thread
    int startRank = id * (noSubsets / P);
    int endRank = (id + 1) * (noSubsets / P);

    // for every rank assigned to current thread, compute the subset and store it in allSUbsets array
    for (int i = startRank; i < endRank; i++) {
        allSubsets[i] = computeSubset(i, N);
    }
}

int main(int argc, char *argv[])
{
    if (argc < 3)  {
        printf("Usage: run_command N P\n");
        exit(-1);
    }

    // parse command line arguments
    N = atoi(argv[1]);
    P = atoi(argv[2]);
    noSubsets = (int)pow(2, N);

    if (noSubsets % P != 0) {
        printf("Current number of threads is NOT a divisor of 2^N!\n");
        exit(-1);
    }

    allSubsets = malloc(noSubsets * sizeof(subset));

    // assign each thread an unique ID
    pthread_t threads[P];
    int threadsID[P];
    for (int i = 0; i < P; i++) {
        threadsID[i] = i;
    }

    // start the threads
    for (int i = 0; i < P; i++)
        pthread_create(&threads[i], NULL, computeSubsetsPerThread, &threadsID[i]);

    // wait for threads to finish their execution
    for (int i = 0; i < P; i++)
        pthread_join(threads[i], NULL);

    printSubsets(allSubsets, noSubsets);

    return 0;
}