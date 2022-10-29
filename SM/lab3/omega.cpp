#include <iostream>
#include <fstream>

using namespace std;

int sh(int i, int n) {
    return (2 * i + ((2 * i) / n)) % n;
}

int main(void) {
    ifstream fin("input1.txt");
    int k, m, i, j, source, destination;

    fin >> k; // numarul de etaje
    fin >> m; // numarul de perechi sursa destinatie

    int n = (1 << k);

    for (i = 0; i < m; i++) {
        fin >> source >> destination;
        cout << source << " -> " << destination << endl;
        int conneciton_type = source ^ destination;
        for (j = k - 1; j >= 0; j--) {
            int shuffle = sh(source, n);
            bool reverse_connection = (((1 << j) == ((1 << j) & conneciton_type)));
            int exit_value = reverse_connection ? ((shuffle % 2 == 0) ? shuffle + 1 : shuffle - 1) : shuffle;
            cout << "Etaj " << j << ": " << shuffle << " -> bloc " << shuffle / 2 << " -> conexiune "
            << (reverse_connection ? "inversa" : "directa") << " -> " << exit_value << endl;
            source = exit_value;
        }
        cout << endl;
    }

    fin.close();

    return 0;
}
