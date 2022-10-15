#include <iostream>
#include <omp.h>
#include <unistd.h>
#include <vector>

#define N 1024

using namespace std;

void print_matrix(vector<vector<int> > a) {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            cout << a[i][j] << " ";
        }
        cout << endl;
    }
    cout << endl;
}

int main()
{
    int i, j, k;
    int sum;
    vector<vector<int> > a (N, vector<int>(N));
    vector<vector<int> > b (N, vector<int>(N));
    vector<vector<int> > c (N, vector<int>(N));
    srand(42);

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            a[i][j] = rand() % 10;
            b[i][j] = rand() % 8 + 1;
        }
    }
    #pragma omp parallel for private(i) default(shared)
    for (i = 0; i < N; i++) {
        #pragma omp parallel for private(j) default(shared)
        for (j = 0; j < N; j++) {
            sum = 0.0;
            #pragma omp parallel for reduction(+ : sum) private(sum) private(k) default(shared)
            for (k = 0; k < N; k++) {
                sum += a[i][k] * b[k][j];
            }
            c[i][j] = sum;
        }
    }

    return 0;
}