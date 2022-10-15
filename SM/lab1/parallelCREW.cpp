#include <iostream>
#include <omp.h>
#include <unistd.h>
#include <vector>
#include <math.h>

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
    int i, j, k, m;
    vector<vector<int> > a (N, vector<int>(N));
    vector<vector<int> > b (N, vector<int>(N));
    vector<vector<vector<int> > > v (N, vector<vector<int > >(N, vector<int>(N)));

    srand(42);

    for (i = 0; i < N; i++) {
        for (j = 0; j < N; j++) {
            a[i][j] = rand() % 10;
            b[i][j] = rand() % 8 + 1;
        }
    }

    #pragma omp parallel for private(i) default(shared)
    for (i = 0; i < N; i++) {
        #pragma omp parallel for private(j) default(shared)
        for (j = 0; j < N; j++) {
            #pragma omp parallel for private(k) default(shared)
            for (k = N - 1; k >= 0; k--) {
                v[i][j][k] = a[i][k] * b[k][j];
                #pragma omp parallel for private(m) default(shared)
                for (m = 0; m < log2(N); m++) {
                    if (k % (1<<(m + 1)) == 0) {
                        v[i][j][k] += v[i][j][k + (1<<m)];
                    }
                }
            }
        }
    }

    // for (i = 0; i < N; i++) {
    //     for (j = 0; j < N; j++) {
    //         cout << v[i][j][0] << " ";
    //     }
    //     cout << endl;
    // }

    return 0;
}