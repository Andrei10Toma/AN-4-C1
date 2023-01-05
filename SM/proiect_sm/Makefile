CC=g++
C_MPI=mpic++
MPI_PROCS=4
MPI_PROCS_HIBRID=4

all: secv openmp pthreads mpi hibrid hibrid_2

secv: secv.o utils.o
	$(CC) $^ -o $@ -g

openmp: openmp.o utils.o
	$(CC) $^ -o $@ -fopenmp -g

pthreads: pthreads.o utils.o
	$(CC) $^ -o $@ -lpthread -g

hibrid: hibrid.cpp utils.o
	$(C_MPI) $^ -o $@ -fopenmp -g

hibrid_2: hibrid_2.cpp utils.o
	$(C_MPI) $^ -o $@ -lpthread -g

mpi: mpi.cpp utils.o
	$(C_MPI) $^ -o $@ -g

secv.o: secv.cpp
	$(CC) -c $< -o $@ -g

utils.o: utils.cpp
	$(CC) -c $< -o $@ -g

openmp.o: openmp.cpp
	$(CC) -c $< -o $@ -fopenmp -g

pthreads.o: pthreads.cpp
	$(CC) -c $< -o $@ -lpthread -g

clean:
	rm -rf *.o secv openmp pthreads mpi hibrid hibrid_2

run_secv: secv
	./secv 50 >> secv_out.txt
	./secv 200 >> secv_out.txt
	./secv 500 >> secv_out.txt
	./secv 1000 >> secv_out.txt
	./secv 1500 >> secv_out.txt
	./secv 2000 >> secv_out.txt
	./secv 2500 >> secv_out.txt
	./secv 3000 >> secv_out.txt

run_openmp: openmp
	./openmp 50 >> openmp_out.txt
	./openmp 200 >> openmp_out.txt
	./openmp 500 >> openmp_out.txt
	./openmp 1000 >> openmp_out.txt
	./openmp 1500 >> openmp_out.txt
	./openmp 2000 >> openmp_out.txt
	./openmp 2500 >> openmp_out.txt
	./openmp 3000 >> openmp_out.txt

run_pthreads: pthreads
	./pthreads 50 >> pthreads_out.txt
	./pthreads 200 >> pthreads_out.txt
	./pthreads 500 >> pthreads_out.txt
	./pthreads 1000 >> pthreads_out.txt
	./pthreads 1500 >> pthreads_out.txt
	./pthreads 2000 >> pthreads_out.txt
	./pthreads 2500 >> pthreads_out.txt
	./pthreads 3000 >> pthreads_out.txt

run_mpi: mpi
	mpirun -np $(MPI_PROCS) --oversubscribe mpi 50 >> mpi_out.txt
	mpirun -np $(MPI_PROCS) --oversubscribe mpi 200 >> mpi_out.txt
	mpirun -np $(MPI_PROCS) --oversubscribe mpi 500 >> mpi_out.txt
	mpirun -np $(MPI_PROCS) --oversubscribe mpi 1000 >> mpi_out.txt 
	mpirun -np $(MPI_PROCS) --oversubscribe mpi 1500 >> mpi_out.txt 
	mpirun -np $(MPI_PROCS) --oversubscribe mpi 2000 >> mpi_out.txt 
	mpirun -np $(MPI_PROCS) --oversubscribe mpi 2500 >> mpi_out.txt 
	mpirun -np $(MPI_PROCS) --oversubscribe mpi 3000 >> mpi_out.txt

run_hibrid: hibrid
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid 50 >> hibrid_out.txt
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid 200 >> hibrid_out.txt
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid 500 >> hibrid_out.txt
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid 1000 >> hibrid_out.txt 
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid 1500 >> hibrid_out.txt 
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid 2000 >> hibrid_out.txt 
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid 2500 >> hibrid_out.txt 
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid 3000 >> hibrid_out.txt

run_hibrid_2: hibrid_2
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid_2 50 >> hibrid_2_out.txt
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid_2 200 >> hibrid_2_out.txt
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid_2 500 >> hibrid_2_out.txt
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid_2 1000 >> hibrid_2_out.txt 
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid_2 1500 >> hibrid_2_out.txt 
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid_2 2000 >> hibrid_2_out.txt 
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid_2 2500 >> hibrid_2_out.txt 
	mpirun -np $(MPI_PROCS_HIBRID) --oversubscribe hibrid_2 3000 >> hibrid_2_out.txt
