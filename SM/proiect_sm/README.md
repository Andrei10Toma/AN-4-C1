# *Proiect SM Toma Andrei 343C1*

## *Overview*

Pentru proiect am ales sa implementez algoritmul Doolittle pentru
factorizarea LU a unei matrici. Am paralelizat problema in 4 feluri:
OpenMP, MPI, Pthreads, hibrid MPI cu OpenMP si hibrid MPI cu Pthreads.
Dimensiunea matricei va fi data ca parametru din linia de comanda. Toate
sursele printeaza timpul de executie al codului paralelizat in fisierele
specificate mai jos. In fisierele cu timpii se printeaza dimensiunea
inputului si timpul de executie al portiunii paralele.

## *Detalii de implementare secvential*
- implementarea se afla in fisierul `secv.cpp`;
- timpii se afla in `secv_out.txt`;
- se genereaza random o matrice patratica de dimensiune data;
- pe matricea generata la pasul precedent se aplica algoritmul de
factorizare LU;
- se parcurge matricea iar pentru fiecare element se genereaza pe rand
elementul din matricea upper si dupa cel din matricea lower.

## *Detalii de implementare OpenMP*
- implementarea se afla in fisierul `openmp.cpp`;
- timpii se afla in `openmp_out.txt`;
- se pornesc threadurile de la inceputul algoritmului si se vor
paraleliza cele 2 bucle din interior care computeaza elementele din
matricile lower si upper;
- am ales paralelizarea buclelor din interior, deoarece elementele
din lower si upper sunt dependente intre ele, asa ca nu ar fi mers o
paralelizare pe bucla cea mare.

## *Detalii de implementare Pthreads*
- implementarea se afla in fisierul `pthreads.cpp`;
- timpii se afla in `pthreads_out.txt`;
- Se creaza NUM_THREADS threaduri care vor executa in paralel aceeasi
zona ca la OpenMP;
- La fiecare iteratie se calculeaza indexii de start si de end ai
fiecarui thread in functie de id-ul, iar pe portiunea determinata de
acesti 2 indexi se face prelucrarea paralela;
- Dupa fiecare computatie de matrice de upper sau lower se va afla o
bariera, deoarece calculul elementelor din lower este dependent de
elementele din upper si reciproc.

## *Detalii de implementare MPI*
- implementarea se afla in fisierul `mpi.cpp`;
- timpii se afla in `mpi_out.txt`;
- Se vor crea MPI_PROCS (definit in Makefile) procese;
- Zona paralelizata este aceeasi ca la Pthreads si OpenMP;
- Fiecare proces isi calculeaza partea lui de matrice, iar dupa vor
trimite catre procesul MASTER, care va construi linia sau coloana din
matricea upper sau lower;

## *Detalii de implementare hibrid MPI cu OpenMP*
- implementarea se afla in fisierul `hibrid.cpp`;
- timpii se afla in `hibrid_out.txt`;
- Se vor crea MPI_PROCS_HIBRID (definit in Makefile) procese;
- Este aceeasi implementare de la mpi, numai ca se paralelizeaza si
calculul de linie sau coloana a matricelor upper sau lower;
- Am paralelizat folosind 4 procese si 2 threaduri, ca altfel nu scala pe
laptopul meu pentru ca are doar 8 threaduri.

## *Detalii de implementare hibrid MPI cu Pthreads*
- implementarea se afla in fisierul `hibrid_2.cpp`;
- timpii se afla in `hibrid_2_out.txt`;
- Aceeasi implementare ca la mpi numai ca e facuta cu Pthreads;
- Aceeasi precizare ca am paralelizat cu 4 procese si 2 threaduri, ca
altfel nu scala.

## *Rulare*
Linux:
- `make` - compilare implementari
- `make run_{secv, openmp, pthreads, mpi, hibrid, hibrid_2}` - ruleaza o
implementare