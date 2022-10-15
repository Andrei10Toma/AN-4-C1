#include "mpi.h"
#include "../aux.h"

void sendSubsetsToMaster(subset *subsets, int subsetsLen) {
    for (int i = 0; i < subsetsLen; i++) {
        MPI_Send(&subsets[i].len, 1, MPI_INT, 0, 0, MPI_COMM_WORLD); // send len of subset
        for (int j = 0; j < subsets[i].len; j++) {
            MPI_Send(&subsets[i].list[j], 1, MPI_INT, 0, j + 1, MPI_COMM_WORLD); // send every number in subset
        }
    }
}

subset *computeSubsets(int startRank, int endRank, int n) {
    subset *result = malloc((endRank - startRank) * sizeof(subset));
    for (int currRank = startRank; currRank < endRank; currRank++) {
        result[currRank - startRank].list = malloc(n * sizeof(int));
        result[currRank - startRank].len = 0;
        subset *currSubset = &result[currRank - startRank];
        for (int j = 0; j < n; j++) {
            if ((1 << j) & currRank) {
                currSubset->list[currSubset->len++] = j + 1;
            }
        }
        currSubset->list = realloc(currSubset->list, currSubset->len * sizeof(int));
    }
    return result;
}


int main (int argc, char *argv[])
{
    int  numtasks, rank, len;
    char hostname[MPI_MAX_PROCESSOR_NAME];
 
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
    MPI_Comm_rank(MPI_COMM_WORLD,&rank);
    MPI_Get_processor_name(hostname, &len);

    if (argc < 2) {
        if (rank == 0) {
            printf("Usage: run_command N\n");
        }
        MPI_Finalize();
        return 0;
    }

    int n  = atoi(argv[1]); // n = len of the root set (e.g. n = 3 -> Set = {1, 2, 3})

    if ((int)pow(2, n) % numtasks != 0) {
        if (rank == 0) {
            printf("Current number of workers is NOT a divisor of 2^N!\n");
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
        int startRank = 0;
        int endRank = (int)pow(2, n) / numtasks;
        subset *subsets = computeSubsets(startRank, endRank, n);

        //store all the subsets
        int allSubsetsLen = (int)pow(2, n);
        subset *allSubsets = malloc(allSubsetsLen * sizeof(subset));

        int z = 0;
        //store subsets computed by the master
        for (int i = 0; i < endRank; i++) {
            allSubsets[z].len = subsets[i].len;
            allSubsets[z].list = subsets[i].list;
            z++;
        }

        //receive and store subsets computed by the slaves
        for (int k = 1; k < numtasks; k++) {
            for (int i = 0; i < endRank - startRank; i++) {
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
        int startRank, endRank;
        MPI_Status status;
        MPI_Recv(&startRank, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
        MPI_Recv(&endRank, 1, MPI_INT, 0, 1, MPI_COMM_WORLD, &status);
        subset *subsets = computeSubsets(startRank, endRank, n);
        sendSubsetsToMaster(subsets, endRank - startRank);
    }
    MPI_Finalize();
}