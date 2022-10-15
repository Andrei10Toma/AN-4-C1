all: send_recv bcast scatter_gather

send_recv:
	mpicc send_recv.c -o send_recv

bcast:
	mpicc bcast.c -o bcast

scatter_gather:
	mpicc scatter_gather.c -o scatter_gather

clean:
	rm send_recv bcast scatter_gather