#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define N 8000
#define NO_THREADS 8

int *a;
int sum[NO_THREADS] = { 0 };
int part = 0;

void* thread_sum(void* arg)
{

    int thread_part = part++;

    for (int i = thread_part * (N / NO_THREADS); i < (thread_part + 1) * (N / NO_THREADS); i++)
        sum[thread_part] += a[i];

    printf("Thread sum is %d\n", sum[thread_part]);
}

int main()
{
    a = malloc(N * sizeof(int));
    for (int i = 0; i < N; i++) {
        a[i] = i;
    }
    pthread_t threads[NO_THREADS];

    for (int i = 0; i < NO_THREADS; i++)
        pthread_create(&threads[i], NULL, thread_sum, (void*)NULL);

    for (int i = 0; i < NO_THREADS; i++)
        pthread_join(threads[i], NULL);

    int total_sum = 0;
    for (int i = 0; i < NO_THREADS; i++)
        total_sum += sum[i];

    printf("Total Sum is %d\n", total_sum);

    return 0;
}