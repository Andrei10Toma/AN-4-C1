#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>

#define NUM_THREADS 4
#define N 20
#define MIN(a,b) (((a)<(b))?(a):(b))

double arr[N];
double sums[NUM_THREADS];

struct thread_info {
    long id;
    double coeficient;
};

void *partial_sums(void *arg) {
    struct thread_info thread_info = *(struct thread_info*) arg;
    long id = thread_info.id;
    int start = id * (double)N / NUM_THREADS;
    int end = MIN((id + 1) * (double)N / NUM_THREADS, N);
    sums[id] = 0;
    for (int i = start; i < end; i++)
        sums[id] += thread_info.coeficient * arr[i];
    printf("Thread %ld calculated %f\n", id, sums[id]);
    pthread_exit(NULL);
}

int main(void) {
    pthread_t threads[NUM_THREADS];
    void *status;
    int i;
    int r;
    struct thread_info arguments[NUM_THREADS];
    double sum = 0;

    for (i = 0; i < N; i++)
        arr[i] = (double) i;

    for (i = 0; i < NUM_THREADS; i++) {
        arguments[i].coeficient = 5.0;
        arguments[i].id = i;
        r = pthread_create(&threads[i], NULL, partial_sums, (void *) &arguments[i]);
        if (r) {
            printf("Eroare la crearea thread-ului %d\n", i);
            exit(-1);
        }
    }

    for (i = 0; i < NUM_THREADS; i++) {
        r = pthread_join(threads[i], &status);
        if (r) {
            printf("Eroare la asteptarea thread-ului %d\n", i);
            exit(-1);
        }
    }

    for (i = 0; i < NUM_THREADS; i++)
        sum += sums[i];

    printf("Sum is %f\n", sum);
    return 0;
}
