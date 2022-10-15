import numpy as np
import math
import matplotlib.pyplot as plt
# Bibliografie: https://www.cs.uky.edu/~jzhang/CS621/chapter7.pdf(pagina 23)
# Overhead Function for adding n numbers on a p(in our case p = n) processor hypercube.

rr = np.arange(1, 32, 0.01)

def O(n):
    return np.add(n, np.multiply(2, np.multiply(n, np.log2(n))))

def T(n):
    return np.add(np.true_divide(n,n), np.multiply(2, np.log2(n)))

def S(n):
    return np.true_divide(n, 1 + np.multiply(2, np.log2(n)))

def E(n):
    return np.true_divide(1, 1 + np.multiply(2, np.log2(n)))

def R(n):
    return 1 + np.multiply(2, np.log2(n))

def U(n):
    return np.true_divide(n,n)

def Q(n):
    return np.true_divide(n, 1 + np.multiply(2, np.log2(n)))

plt.plot(rr, U(rr).astype(np.double))
plt.plot(rr, E(rr).astype(np.double))
plt.legend(['U(n)','E(n)'])
plt.show()

# Obs: Q == S -> se vor suprapune -> fac alt grafic
plt.plot(rr, S(rr).astype(np.double))
plt.plot(rr, R(rr).astype(np.double))
plt.legend(['S(n)','R(n)'])
plt.show()

plt.plot(rr, Q(rr).astype(np.double))
plt.legend(['Q(n)'])
plt.show()
