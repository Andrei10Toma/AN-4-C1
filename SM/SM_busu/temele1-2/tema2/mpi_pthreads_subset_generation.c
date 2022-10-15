#include "mpi.h"
#include <pthread.h>
#include "../aux.h"

subset *workerSubsets;
int n, p, workerStartRank, workerEndRank;

void sendSubsetsToMaster(subset *subsets, int subsetsLen) {
    for (int i = 0; i < subsetsLen; i++) {
        MPI_Send(&subsets[i].len, 1, MPI_INT, 0, 0, MPI_COMM_WORLD); // send len of subset
        for (int j = 0; j < subsets[i].len; j++) {
            MPI_Send(&subsets[i].list[j], 1, MPI_INT, 0, j + 1, MPI_COMM_WORLD); // send every number in subset
        }
    }
}

void *computeSubsetsPerThread(void *argument) {
    //every thread will compute subsets in range [start, end]

    int id = *(int *)argument;

    //compute start and end ranks for each thread
    int start = workerStartRank + id * ((workerEndRank - workerStartRank) /  p);
    int end = workerStartRank + (id + 1) * ((workerEndRank - workerStartRank) /  p);

    // for every rank assigned to current thread, compute the subset and store it in allSubsets array
    for (int i = start; i < end; i++) {
        workerSubsets[i - workerStartRank] = computeSubset(i, n);
    }
}

subset *computeSubsets() {
    pthread_t threads[p];
    int threadsArg[p];
    for (int i = 0; i < p; i++) {
        threadsArg[i] = i;
    }

    workerSubsets = malloc((workerEndRank - workerStartRank) *  sizeof(subset));

    // start the threads
    for (int i = 0; i < p; i++)
        pthread_create(&threads[i], NULL, computeSubsetsPerThread, &threadsArg[i]);

    // wait for threads to finish their execution
    for (int i = 0; i < p; i++)
        pthread_join(threads[i], NULL);

    return workerSubsets;
}

int main (int argc, char *argv[])
{
    int  numtasks, rank, len;
    char hostname[MPI_MAX_PROCESSOR_NAME];
 
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
    MPI_Comm_rank(MPI_COMM_WORLD,&rank);
    MPI_Get_processor_name(hostname, &len);

    //parse command line arguments
    if (argc < 3) {
        if (rank == 0) {
            printf("Usage: run_command N P\n");
        }
        MPI_Finalize();
        return 0;
    }

    n  = atoi(argv[1]); // n = len of the root set (e.g. n = 3 -> Set = {1, 2, 3})
    p = atoi(argv[2]); // p = number of threads per worker

    if ((int)pow(2, n) % numtasks != 0) {
        if (rank == 0) {
            printf("Current number of workers is NOT a divisor of 2^N!\n");
        }
        MPI_Finalize();
        return 0;
    }

    if (((int)pow(2, n) / numtasks) % p != 0) {
        if (rank == 0) {
            printf("Current number of threads is NOT a divisor of 2^N/N!\n");
        }
        MPI_Finalize();
        return 0;
    }
 
    if (rank == 0) { // master execution

        //send to slaves the range of ranks that should be computed
        for (int i = 1; i < numtasks; i++) {
            int startRank = i * ((int)pow(2, n) / numtasks);
            int endRank = (i + 1) * ((int)pow(2, n) / numtasks);
            MPI_Send(&startRank, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
            MPI_Send(&endRank, 1, MPI_INT, i, 1, MPI_COMM_WORLD);
        }

        //compute own range
        workerStartRank = 0;
        workerEndRank = (int)pow(2, n) / numtasks;
        subset *subsets = computeSubsets();

        //store all the subsets
        int allSubsetsLen = (int)pow(2, n);
        subset *allSubsets = malloc(allSubsetsLen * sizeof(subset));

        int z = 0;
        //append subsets computed by the master
        for (int i = 0; i < workerEndRank; i++) {
            allSubsets[z].len = subsets[i].len;
            allSubsets[z].list = subsets[i].list;
            z++;
        }

        //receive and append subsets computed by the slaves
        for (int k = 1; k < numtasks; k++) {
            for (int i = 0; i < workerEndRank - workerStartRank; i++) {
                int len;
                MPI_Status status;
                MPI_Recv(&len, 1, MPI_INT, k, 0, MPI_COMM_WORLD, &status);
                allSubsets[z].len = len;
                allSubsets[z].list = malloc (len * sizeof(int));
                for (int j = 0; j < len; j++) {
                    int subsetMember;
                    MPI_Recv(&subsetMember, 1, MPI_INT, k, j + 1, MPI_COMM_WORLD, &status);
                    allSubsets[z].list[j] = subsetMember;
                }
                z++;
            }
        }

        printSubsets(allSubsets, allSubsetsLen);

    } else { //slave executuion
        MPI_Status status;
        MPI_Recv(&workerStartRank, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
        MPI_Recv(&workerEndRank, 1, MPI_INT, 0, 1, MPI_COMM_WORLD, &status);
        subset *subsets = computeSubsets();
        sendSubsetsToMaster(subsets, workerEndRank - workerStartRank);
    }
    MPI_Finalize();
}