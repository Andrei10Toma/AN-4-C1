CC=g++
C_MPI=mpic++
MPI_PROCS=4

all: secv openmp pthreads mpi hibrid

secv: secv.o utils.o
	$(CC) $^ -o $@

openmp: openmp.o utils.o
	$(CC) $^ -o $@ -fopenmp

pthreads: pthreads.o utils.o
	$(CC) $^ -o $@ -lpthread

hibrid: hibrid.cpp utils.o
	$(C_MPI) $^ -o $@ -fopenmp

mpi: mpi.cpp utils.o
	$(C_MPI) $^ -o $@

secv.o: secv.cpp

utils.o: utils.cpp

openmp.o: openmp.cpp
	$(CC) -c $< -o $@ -fopenmp

pthreads.o: pthreads.cpp
	$(CC) -c $< -o $@ -lpthread

clean:
	rm -rf *.o secv openmp pthreads mpi hibrid *.txt

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
	mpirun -np $(MPI_PROCS) --oversubscribe hibrid 50 >> hibrid_out.txt
	mpirun -np $(MPI_PROCS) --oversubscribe hibrid 200 >> hibrid_out.txt
	mpirun -np $(MPI_PROCS) --oversubscribe hibrid 500 >> hibrid_out.txt
	mpirun -np $(MPI_PROCS) --oversubscribe hibrid 1000 >> hibrid_out.txt 
	mpirun -np $(MPI_PROCS) --oversubscribe hibrid 1500 >> hibrid_out.txt 
	mpirun -np $(MPI_PROCS) --oversubscribe hibrid 2000 >> hibrid_out.txt 
	mpirun -np $(MPI_PROCS) --oversubscribe hibrid 2500 >> hibrid_out.txt 
	mpirun -np $(MPI_PROCS) --oversubscribe hibrid 3000 >> hibrid_out.txt
