#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
 
#define  ROOT 0
#define CHUNCK_SIZE 5
 
int main (int argc, char *argv[]) {

    /*
     * Problema implementata este suma elementelor dintr-un vector, iar procesul Master va distribui
     * un coeficient workerilor cu care acestia vor inmulti suma partiala, dupa ce workerii au facut
     * suma masterul aduna sumele partiale si afiseaza rezultatul.
     */

    int rank, proc, coeficient;

    double* arr;
    double* process_arr;
    double* result_arr;
    
    MPI_Init(&argc, &argv);
    
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &proc);

    if (rank == ROOT) {
        arr = malloc (CHUNCK_SIZE * proc * sizeof(double));
        for (int i = 0; i < proc * CHUNCK_SIZE; ++i) {
            arr[i] = (double)i;
        }
        coeficient = 5;
    }

    MPI_Bcast(&coeficient, 1, MPI_INT, ROOT, MPI_COMM_WORLD);
    process_arr = malloc(CHUNCK_SIZE * sizeof(double));
    MPI_Scatter(arr, CHUNCK_SIZE, MPI_DOUBLE, process_arr, CHUNCK_SIZE, MPI_DOUBLE, ROOT, MPI_COMM_WORLD);

    double sum = 0;

    for (int i = 0; i < CHUNCK_SIZE; i++) {
        sum += coeficient * process_arr[i];
    }

    if (rank == ROOT) {
        result_arr = malloc(proc * sizeof(double));
    }

    MPI_Gather(&sum, 1, MPI_DOUBLE, result_arr, 1, MPI_DOUBLE, ROOT, MPI_COMM_WORLD);
    if (rank == ROOT) {
        double sum = 0;
        for (int i = 0; i < proc; i++) {
            sum += result_arr[i];
        }
        printf("%f\n", sum);
    }

    if (rank == ROOT) {
        free(arr);
        free(result_arr);
    }

    free(process_arr);

    MPI_Finalize();
    return 0;
}
