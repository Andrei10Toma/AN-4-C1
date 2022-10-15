import math
# partea Hardware este incarcata de Alexandru Dragus 344C1

def shuffle(i, N):
    return (2*i + int(2*i/N)) % N

def transmision(i, N):
    if i % 2 == 0:
        return [i, i + 1];

    if i % 2 == 1:
        return [i - 1, i];

def printList(list, N):
    for i in range(0, len(list), 2):
        print("Etajul ", int(len(list)/2 - i/2 - 1), ":");
        print("    Blocul ", int(list[i] / 2));
        print("    Valoare Permutare shufle: ", list[i])
        if list[i] == list[i + 1]:
            print("    Conexiune Directa")
        else:
            print("    Conexiune Inversa")


def Omega(N, input, output, currFloor, currPath):

    if input == output and currFloor == 0:
        printList(currPath, N)

    if currFloor == 0:
        return ;

    currOutput = shuffle(input, N);
    currTransmision = transmision(currOutput, N);

    for trans in currTransmision:
        path = currPath.copy()
        path.append(currOutput)
        path.append(trans)
        Omega(N, trans, output, currFloor - 1, path)


def main():
    n = int(input("n(power of 2) = "));
    m = int(input("m = "));

    for i in range(m):
        Omega(n, int(input("source = ")), int(input("destination = ")), math.log2(n), [])

if __name__ == '__main__':
    main()