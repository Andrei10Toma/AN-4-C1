#include <stdio.h>
#include <stdlib.h>
#include "math.h"

typedef struct subset {
    int len;
    int *list;
}subset;

void printSubset(subset sub) {
    int i;
    printf("{ ");
    for (i = 0; i < sub.len; i++) {
        printf("%d ", sub.list[i]);
    }
    printf("}\n");
}

void printSubsets(subset *allSubsets, int allSubsetsLen) {
    // for (int i = 0; i < allSubsetsLen; i++) {
    //     printSubset(allSubsets[i]);
    // }
}

subset computeSubset(int rank, int n) {
    int j;
    subset result;
    result.list = malloc(n * sizeof(int));
    result.len = 0;
    for (j = 0; j < n; j++) {
        if ((1 << j) & rank) {
            result.list[result.len++] = j + 1;
        }
    }
    result.list = realloc(result.list, result.len * sizeof(int));

    return result;
}

