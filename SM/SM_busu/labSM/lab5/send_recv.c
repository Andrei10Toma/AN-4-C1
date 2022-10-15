#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
 
int main (int argc, char *argv[])
{
    int  numtasks, rank, len;
    char hostname[MPI_MAX_PROCESSOR_NAME];
 
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numtasks); // Total number of processes.
    MPI_Comm_rank(MPI_COMM_WORLD,&rank); // The current process ID / Rank.
    MPI_Get_processor_name(hostname, &len);
 
    int num = rand();
 
    if (rank == 0) {
        printf("Process with rank %d is sending %d to all others.\n", rank, num);

        for (int i = 1; i < numtasks; i++)
            MPI_Send(&num, 1, MPI_INT, i, 0, MPI_COMM_WORLD);

    } else {
        MPI_Status status;
        MPI_Recv(&num, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
        printf("Process with rank %d, received %d with tag %d.\n", rank, num, status.MPI_TAG);
    }

    MPI_Finalize();
 
}