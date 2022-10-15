import numpy as np
import math
import matplotlib.pyplot as plt

rr = np.arange(1, 32, 0.01)

def O(n):
    return np.add(np.power(n,3), np.multiply(np.power(n,2), np.log2(n)))

def T(n):
    return np.multiply(4, (np.true_divide(np.power(n,3), (n + 3))))

def S(n):
    return np.true_divide(T(1), T(n))

def E(n):
    return np.true_divide((n + 3), 4*n)

def R(n):
    return np.true_divide(n + np.log2(n), n)

def U(n):
    return np.true_divide(np.multiply(n + 3, n + np.log2(n)), np.multiply(4, np.power(n,2)))

def Q(n):
    return np.true_divide(np.power(n+3,2), np.multiply(16, np.add(n, np.log2(n))))


plt.plot(rr, U(rr).astype(np.double))
plt.plot(rr, E(rr).astype(np.double))
plt.legend(['U(n)','E(n)'])
plt.show()

plt.plot(rr, S(rr).astype(np.double))
plt.plot(rr, R(rr).astype(np.double))
plt.plot(rr, Q(rr).astype(np.double))
plt.legend(['S(n)','R(n)',' Q(n)'])
plt.show()