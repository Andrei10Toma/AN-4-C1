#include <vector>
#include <omp.h>
#include <iostream>

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

int main(void) {
    int i, j, k;
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

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            for (int k = 0; k < N; k++) {
                c[i][j] += a[i][k] * b[k][j];
            }
        }
    }
    
    // print_matrix(a);
    // print_matrix(b);
    // print_matrix(c);

    return 0;
}