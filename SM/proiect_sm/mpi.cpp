#include <stdio.h>
#include <stdlib.h>
#include <cstring>
#include <string.h>
#include "utils.h"
#include <chrono>
#include <time.h>
#include <iostream>
#include "mpi.h"

#define MIN(a,b) (((a)<(b))?(a):(b))

#define MASTER 0

using namespace std;
using namespace std::chrono;


void luDecomposition(double **matrix, double **lower, double **upper, int n, int rank, int num_procs) {

	for (int i = 0; i < n; i++) {
        int start = rank * ((double) (n - i)) / num_procs;
        int end = MIN((int)((rank + 1) * ((double) (n - i)) / num_procs), n - i);
		for (int k = i + start; k < i + end; k++) {
			double sum = 0.0;
			for (int j = 0; j < i; j++)
				sum += (lower[i][j] * upper[j][k]);

			upper[i][k] = matrix[i][k] - sum;
		}

        if (rank == MASTER) {
            for (int j = 1; j < num_procs; j++) {
                int proc_start = j * ((double) (n - i)) / num_procs;
                int proc_end = MIN((int)((j + 1) * ((double) (n - i)) / num_procs), n - i);
                MPI_Recv(&(upper[i][proc_start + i]), proc_end - proc_start, MPI_DOUBLE, j, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            }
            for (int j = 1; j < num_procs; j++) {
                MPI_Send(&(upper[i][0]), n, MPI_DOUBLE, j, 0, MPI_COMM_WORLD);
            }
            
        } else {
            MPI_Send(&(upper[i][i + start]), end - start, MPI_DOUBLE, MASTER, 0, MPI_COMM_WORLD);
            MPI_Recv(&(upper[i][0]), n, MPI_DOUBLE, MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        }

        double *column = (double *) calloc(n, sizeof(double));
		for (int k = i + start; k < i + end; k++) {
			if (i == k) {
				lower[i][i] = 1;
			} else {
				double sum = 0;
				for (int j = 0; j < i; j++)
					sum += (lower[k][j] * upper[j][i]);

				lower[k][i] = (matrix[k][i] - sum) / upper[i][i];
			}
            column[k - start - i] = lower[k][i];
		}

        double *full_column = (double *) calloc(n, sizeof(double));
        if (rank == MASTER) {
            for (int j = 1; j < num_procs; j++) {
                int proc_start = j * ((double) (n - i)) / num_procs;
                int proc_end = MIN((int)((j + 1) * ((double) (n - i)) / num_procs), n - i);
                MPI_Recv(column, proc_end - proc_start, MPI_DOUBLE, j, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
                for (int k = i + proc_start; k < i + proc_end; k++)
                    lower[k][i] = column[k - proc_start - i];
            }
            for (int j = 0; j < n; j++)
                full_column[j] = lower[j][i];
            for (int j = 1; j < num_procs; j++) {
                MPI_Send(full_column, n, MPI_DOUBLE, j, 0, MPI_COMM_WORLD);
            }
            
        } else {
            MPI_Send(column, end - start, MPI_DOUBLE, MASTER, 0, MPI_COMM_WORLD);
            MPI_Recv(full_column, n, MPI_DOUBLE, MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            for (int j = 0; j < n; j++)
                lower[j][i] = full_column[j];
        }
	}
}

int main(int argc, char **argv)
{
    int rank, num_procs;
    std::chrono::_V2::system_clock::time_point start;
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &num_procs);

	if (argc < 2)
		throw_error(strdup("Insufficient arguments!"));

    double **matrix;
    double **lower;
    double **upper;
    int elements = atoi(argv[1]);
    matrix = allocate_square_matrix(elements);
    lower = allocate_square_matrix(elements);
    upper = allocate_square_matrix(elements);
    if (rank == MASTER) {
        srand(42);

	    initialize_random_matrix(matrix, elements);
        for (int i = 1; i < num_procs; i++) {
            for (int j = 0; j < elements; j++)
                MPI_Send(&(matrix[j][0]), elements, MPI_DOUBLE, i, 0, MPI_COMM_WORLD);
        }
    } else {
        for (int j = 0; j < elements; j++)
            MPI_Recv(&(matrix[j][0]), elements, MPI_DOUBLE, MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    }

    clock_t t;
    if (rank == MASTER) {
        start = high_resolution_clock::now();
    }

    luDecomposition(matrix, lower, upper, elements, rank, num_procs);

    if (rank == MASTER) {
        auto end = high_resolution_clock::now();
        // print_matrix(lower, elements);
        // cout << endl;
        // print_matrix(upper, elements);
        auto duration = duration_cast<milliseconds>(end - start);
        cout << elements << " " << (double)duration.count() / 1000.0 << endl;
    }
    MPI_Finalize();
	return 0;
}
