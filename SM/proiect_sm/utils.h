#ifndef _LU_DECOMPOSITION_
#define _LU_DECOMPOSITION_

void throw_error(char *message);

double **allocate_square_matrix(int size);

void initialize_random_matrix(double **matrix, int elements);

void print_matrix(double **matrix, int n);

#endif
