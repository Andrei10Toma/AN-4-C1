#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>

#define ROOT 0

int main (int argc, char *argv[])
{
    int  numtasks, rank;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
    MPI_Comm_rank(MPI_COMM_WORLD,&rank);

    int num;

    // Root process generates a random number.
    if(rank == 0){
        num = rand();
    }

    // Broadcasts to all processes.
    MPI_Bcast(&num, 1, MPI_INT, 0, MPI_COMM_WORLD);

    printf("Process %d, received from bcast %d.\n", rank, num);

    MPI_Finalize();

}
