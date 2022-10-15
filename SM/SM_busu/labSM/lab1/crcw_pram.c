#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>

#include "matrix.h"

void *for3(void *arg) {
  	struct parrent thread_id = *(struct parrent*) arg;

    //step 1
    p[thread_id.i][thread_id.j][thread_id.k] = a[thread_id.i][thread_id.k] * b[thread_id.k][thread_id.j];
}

void *for2(void *arg) {
  	struct parrent thread_id = *(struct parrent*) arg;

    pthread_t threads[n];
  	int r;
  	long id;
  	void *status;
  	struct parrent arguments[n];

    for (id = 0; id < n; id++) {
  		arguments[id].i = thread_id.i;
        arguments[id].j = thread_id.j;
        arguments[id].k = id;
		r = pthread_create(&threads[id], NULL, for3, &arguments[id]);

		if (r) {
	  		printf("Eroare la crearea thread-ului %ld\n", id);
	  		exit(-1);
		}
  	}

  	for (id = 0; id < n; id++) {
		r = pthread_join(threads[id], &status);

		if (r) {
	  		printf("Eroare la asteptarea thread-ului %ld\n", id);
	  		exit(-1);
		}
  	}

    //step 2
    c[thread_id.i][thread_id.j] = 0;
    for(id = 0; id < n; id++) {
        c[thread_id.i][thread_id.j] += p[thread_id.i][thread_id.j][id];
    }

}

void *for1(void *arg) {
  	long thread_id = *(long*) arg;

    pthread_t threads[n];
  	int r;
  	long id;
  	void *status;
  	struct parrent arguments[n];

    for (id = 0; id < n; id++) {
  		arguments[id].i = thread_id;
        arguments[id].j = id;
        arguments[id].k = -1;
		r = pthread_create(&threads[id], NULL, for2, &arguments[id]);

		if (r) {
	  		printf("Eroare la crearea thread-ului %ld\n", id);
	  		exit(-1);
		}
  	}

  	for (id = 0; id < n; id++) {
		r = pthread_join(threads[id], &status);

		if (r) {
	  		printf("Eroare la asteptarea thread-ului %ld\n", id);
	  		exit(-1);
		}
  	}

}

int main(int argc, char *argv[]) {
    initializeMatrix();

	pthread_t threads[n];
  	int r;
  	long id;
  	void *status;
  	long arguments[n];

  	for (id = 0; id < n; id++) {
  		arguments[id] = id;
		r = pthread_create(&threads[id], NULL, for1, &arguments[id]);

		if (r) {
	  		printf("Eroare la crearea thread-ului %ld\n", id);
	  		exit(-1);
		}
  	}

  	for (id = 0; id < n; id++) {
		r = pthread_join(threads[id], &status);

		if (r) {
	  		printf("Eroare la asteptarea thread-ului %ld\n", id);
	  		exit(-1);
		}
  	}

	printMatrixes();

  	pthread_exit(NULL);
}