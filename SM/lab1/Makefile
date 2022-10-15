all: secv paralelCRCW parallelCREW run

secv: secv.cpp
	g++ secv.cpp -o secv

paralelCRCW: paralelCRCW.cpp
	g++ paralelCRCW.cpp -o paralelCRCW

parallelCREW: parallelCREW.cpp
	g++ parallelCREW.cpp -o parallelCREW

run:
	time ./secv
	time ./paralelCRCW
	time ./parallelCREW