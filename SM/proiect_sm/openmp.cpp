#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "utils.h"
#include <iostream>
#include <chrono>

using namespace std;
using namespace std::chrono;

void luDecomposition(double **matrix, double **lower, double **upper, int n) {
    #pragma omp parallel shared(matrix, lower, upper)
    {
        for (int i = 0; i < n; i++) {
            #pragma omp for schedule(static, 4)
            for (int k = i; k < n; k++) {
                double sum = 0;
                for (int j = 0; j < i; j++)
                    sum += (lower[i][j] * upper[j][k]);
    
                upper[i][k] = matrix[i][k] - sum;
            }

            #pragma omp for schedule(static, 4)
            for (int k = i; k < n; k++) {
                if (i == k) {
                    lower[i][i] = 1; 
                } else {
                    double sum = 0;
                    for (int j = 0; j < i; j++)
                        sum += (lower[k][j] * upper[j][i]);
    
                    lower[k][i] = (matrix[k][i] - sum) / upper[i][i];
                }
            }
        }
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
    // cout << elements << " " << double(duration.count()) / 1000.0 << endl;
    print_matrix(lower, elements);
    cout << endl;
    print_matrix(upper, elements);

	return 0;
}
