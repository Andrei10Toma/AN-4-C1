CC=g++

all: secv openmp pthreads

secv: secv.o utils.o
	$(CC) $^ -o $@

openmp: openmp.o utils.o
	$(CC) $^ -o $@ -fopenmp

pthreads: pthreads.o utils.o
	$(CC) $^ -o $@ -lpthread

secv.o: secv.cpp

utils.o: utils.cpp

openmp.o: openmp.cpp
	$(CC) -c $< -o $@ -fopenmp

pthreads.o: pthreads.cpp
	$(CC) -c $< -o $@ -lpthread

clean:
	rm -rf *.o secv openmp pthreads

run_secv: secv
	./secv 50 > secv_outputs/50.out
	./secv 200 > secv_outputs/200.out
	./secv 500 > secv_outputs/500.out
	./secv 1000 > secv_outputs/1000.out
	./secv 1500 > secv_outputs/1500.out
	./secv 2000 > secv_outputs/2000.out
	./secv 2400 > secv_outputs/2400.out

run_openmp: openmp
	./openmp 50 > openmp_outputs/50.out
	./openmp 200 > openmp_outputs/200.out
	./openmp 500 > openmp_outputs/500.out
	./openmp 1000 > openmp_outputs/1000.out
	./openmp 1500 > openmp_outputs/1500.out
	./openmp 2000 > openmp_outputs/2000.out
	./openmp 2400 > openmp_outputs/2400.out

run_pthreads: pthreads
	./pthreads 50 > pthreads_outputs/50.out
	./pthreads 200 > pthreads_outputs/200.out
	./pthreads 500 > pthreads_outputs/500.out
	./pthreads 1000 > pthreads_outputs/1000.out
	./pthreads 1500 > pthreads_outputs/1000.out
	./pthreads 2000 > pthreads_outputs/2000.out
	./pthreads 2400 > pthreads_outputs/2400.out
