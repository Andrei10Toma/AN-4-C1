CC=g++

all: secv openmp

secv: secv.o utils.o
	$(CC) $^ -o $@

openmp: openmp.o utils.o
	$(CC) $^ -o $@ -fopenmp

secv.o: secv.cpp

utils.o: utils.cpp

openmp.o: openmp.cpp

clean:
	rm -rf *.o secv openmp

run_secv: secv
	./secv 50 > secv_outputs/50.out
	./secv 75 > secv_outputs/75.out
	./secv 100 > secv_outputs/100.out
	./secv 200 > secv_outputs/200.out
	./secv 500 > secv_outputs/500.out
	./secv 750 > secv_outputs/750.out
	./secv 900 > secv_outputs/900.out
	./secv 1000 > secv_outputs/1000.out
	./secv 2000 > secv_outputs/2000.out
	./secv 2200 > secv_outputs/2200.out
	./secv 2400 > secv_outputs/2400.out
	./secv 3000 > secv_outputs/3000.out

run_openmp: openmp
	./openmp 50 > openmp_outputs/50.out
	./openmp 75 > openmp_outputs/75.out
	./openmp 100 > openmp_outputs/100.out
	./openmp 200 > openmp_outputs/200.out
	./openmp 500 > openmp_outputs/500.out
	./openmp 750 > openmp_outputs/750.out
	./openmp 900 > openmp_outputs/900.out
	./openmp 1000 > openmp_outputs/1000.out
	./openmp 2000 > openmp_outputs/2000.out
	./openmp 2200 > openmp_outputs/2200.out
	./openmp 2400 > openmp_outputs/2400.out
	./openmp 3000 > openmp_outputs/3000.out
