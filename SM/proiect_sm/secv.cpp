#include <iostream>
#include <string.h>
#include "utils.h"
#include "omp.h"

using namespace std;

void lu_decomposition(double **a, double **l, double **u, int n) {
    int i = 0, j = 0, k = 0;
    for (i = 0; i < n; i++) {
        for (j = i; j < n; j++) {
            l[j][i] = a[j][i];
            for (k = 0; k < i; k++)
                l[j][i] -= l[j][k] * u[k][i];
        }

        for (j = i; j < n; j++) {
            if (j == i)
                u[i][j] = (double) 1;
            else {
                u[i][j] = a[i][j] / l[i][i];
                for (k = 0; k < i; k++)
                    u[i][j] -= ((l[i][k] * u[k][j]) / l[i][i]);
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

	lu_decomposition(matrix, lower, upper, elements);
    print_matrix(lower, elements);
    cout << endl;
    print_matrix(upper, elements);

	return 0;
}
