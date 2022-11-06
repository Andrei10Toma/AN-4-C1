connection_blocks = []

def compute_c2_pair(comp: int, permutation: list):
    for i, e in enumerate(permutation):
        if e == comp:
            return (i, e)


def compute_c1_pair(comp: int, permutation: list):
    return (comp, permutation[comp])


def compute_next_available_pair(vis: set, permutation: list):
    for i, e in enumerate(permutation):
        if (i, e) not in vis:
            return (i, e)


def print_connection():
    check_connection = lambda x: 'dir' if x else 'ind'
    for connection_block in connection_blocks:
        for connection in connection_block:
            print(check_connection(connection), end=', ')
        print()

def benes(permutation: list, left_level_index: int, right_level_index: int, left_block_index: int, right_block_index: int):
    vis = set()
    complement = lambda x: x + 1 if x % 2 == 0 else x - 1
    c1 = [ (0, permutation[0]) ]
    vis.add((0, permutation[0]))
    c2 = []
    n = len(permutation)
    while len(vis) != n - 1:
        c2_pair = compute_c2_pair(complement(c1[-1][1]), permutation)
        c2.append(c2_pair)
        vis.add(c2_pair)
        c1_pair = compute_c1_pair(complement(c2[-1][0]), permutation)
        if c1_pair in vis:
            c1_pair = compute_next_available_pair(vis, permutation)
        c1.append(c1_pair)
        vis.add(c1_pair)
    c2.append(compute_c2_pair(complement(c1[-1][1]), permutation))
    if len(c1) != 1:
        for pair in c1:
            connection_blocks[int(pair[0] / 2) + left_block_index][left_level_index] = (pair[0] % 2 == 0)
            connection_blocks[int(pair[1] / 2) + right_block_index][(- 1 - right_level_index)] = (pair[1] % 2 == 0)
    else:
        if c1[0][0] == c1[0][1]:
            connection_blocks[left_block_index][left_level_index] = True
        else:
            connection_blocks[left_block_index][left_level_index] = False
        return
    c1.sort(key=lambda x: x[0])
    c2.sort(key=lambda x: x[0])
    permutation_1 = [ int(pair[1] / 2) for pair in c1 ]
    benes(permutation_1, left_level_index + 1, right_level_index + 1, left_block_index, right_level_index)
    permutation_2 = [ int(pair[1] / 2) for pair in c2 ]
    benes(permutation_2, left_level_index + 1, right_level_index + 1, int(left_block_index + len(permutation_2) / 2), int(right_block_index + len(permutation_2) / 2))


if __name__ == '__main__':
    with open('input1.txt', 'r') as f:
        lines = f.readlines()
        k = int(lines[0][:-1])
        connection_blocks = [[False for i in range(2 * k - 1)] for j in range(4)]
        permutation = [ int(x[:-1]) for x in lines[1:] ]
        benes(permutation, 0, 0, 0, 0)
        print_connection()
