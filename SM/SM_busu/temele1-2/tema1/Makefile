all: mpi_subset_generation openmp_subset_generation

mpi_subset_generation: mpi_subset_generation.c
	mpicc mpi_subset_generation.c -o mpi_subset_generation -lm

openmp_subset_generation: openmp_subset_generation.c
	gcc -o openmp_subset_generation -fopenmp openmp_subset_generation.c -lm

clean:
	rm mpi_subset_generation openmp_subset_generation