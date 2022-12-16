#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "utils.h"
#include <iostream>
#include <pthread.h>
#include <math.h>
#include <chrono>

#define NUM_THREADS 8
#define MIN(a,b) (((a)<(b))?(a):(b))

using namespace std;
using namespace std::chrono;

pthread_barrier_t barrier;

struct thread_struct {
    double **matrix;
    double **lower;
    double **upper;
    int n;
    int thread_id;
    int iteration;
};

void *thread_fn(void *arg) {
    struct thread_struct argument = * (struct thread_struct *) arg;
    int id = argument.thread_id;
    int n = argument.n;
    double **upper = argument.upper;
    double **lower = argument.lower;
    double **matrix = argument.matrix;

    for (int i = 0; i < n; i++) {
        int start = id * ((double)(n - i) / NUM_THREADS);
        int end = MIN((id + 1) * (double)(n - i) / NUM_THREADS, (double) (n - i));
        for (int k = i + start; k < i + end; k++) {
            double sum = 0;
            for (int j = 0; j < i; j++)
                sum += (lower[i][j] * upper[j][k]);

            upper[i][k] = matrix[i][k] - sum;
        }
    
        pthread_barrier_wait(&barrier);
    
        for (int k = i + start; k < i + end; k++) {
            if (i == k) {
                lower[i][i] = 1; 
            } else {
                double sum = 0;
                for (int j = 0; j < i; j++)
                    sum += (lower[k][j] * upper[j][i]);
    
                lower[k][i] = (matrix[k][i] - sum) / upper[i][i];
            }
        }

        pthread_barrier_wait(&barrier);
    }

    pthread_exit(NULL);
}

void luDecomposition(double **matrix, double **lower, double **upper, int n) {
    int r;
    r = pthread_barrier_init(&barrier, NULL, NUM_THREADS);
    if (r)
        throw_error(strdup("Can't initialize barrier"));

    struct thread_struct arguments[NUM_THREADS];
    for (int i = 0; i < NUM_THREADS; i++) {
        arguments[i].n = n;
        arguments[i].matrix = matrix;
        arguments[i].lower = lower;
        arguments[i].upper = upper;
        arguments[i].thread_id = i;
    }

    pthread_t threads[NUM_THREADS];
    for (int j = 0; j < NUM_THREADS; j++) {
        r = pthread_create(&threads[j], NULL, thread_fn, (void *) &arguments[j]);
        if (r)
            throw_error(strdup("Error creating thread"));
    }

    for (int j = 0; j < NUM_THREADS; j++) {
        r = pthread_join(threads[j], NULL);
        if (r)
            throw_error(strdup("Failed creating thread"));
    }
}

int main(int argc, char **argv)
{
	if (argc < 2)
		throw_error(strdup("Insufficient arguments!"));

	srand(42);

	int elements = atoi(argv[1]);

	double **matrix = allocate_square_matrix(elements);
	double **lower = allocate_square_matrix(elements);
	double **upper = allocate_square_matrix(elements);

	initialize_random_matrix(matrix, elements);

    auto start = high_resolution_clock::now();
	luDecomposition(matrix, lower, upper, elements);
    auto end = high_resolution_clock::now();
    auto duration = duration_cast<milliseconds>(end - start);
    // cout << elements << " " << (double)duration.count() / 1000.0 << endl;
    print_matrix(lower, elements);
    cout << endl;
    print_matrix(upper, elements);

	return 0;
}
