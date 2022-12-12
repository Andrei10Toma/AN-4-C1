#include <stdlib.h>
#include <string.h>
#include <iostream>
#include "utils.h"

using namespace std;

void throw_error(char *message) {
    cerr << message << endl;
    exit(-1);
}

double **allocate_square_matrix(int size) {
    double **matrix = (double **)calloc(sizeof(double *), size);
	if (matrix == NULL) 
		throw_error(strdup("Unable to allocate memory for matrix!"));
	
	for (int i = 0; i < size; ++i) {
		matrix[i] = (double *)calloc(sizeof(double), size);
		if (matrix[i] == NULL)
			throw_error(strdup("Unable to allocate memory for matrix!"));
	}   

    return matrix;
}

void initialize_random_matrix(double **matrix, int elements) {
    for (int i = 0; i < elements; ++i)
		for (int j = 0; j < elements; ++j)
			matrix[i][j] = rand() % 10 + 10;
}

void print_matrix(double **matrix, int n) {
    for (int i = 0; i < n; i++) {
		for (int j = 0; j < n; j++)
			cout << matrix[i][j] << " ";
		cout << endl;
	}   
}
