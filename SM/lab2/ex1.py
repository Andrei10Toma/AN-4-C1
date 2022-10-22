import numpy as np
import matplotlib.pyplot as plt 

n = np.arange(1, 32, 0.1)

sn = (n + 3) / 4
en = np.divide(n + 3, 4 * n)
rn = np.divide(np.add(n, np.log2(n)), n)
un = np.divide(np.multiply(n + 3, np.add(n, np.log2(n))), 4 * np.power(n, 2))
qn = np.divide(np.power(n + 3, 2), 16 * np.add(n, np.log2(n)))

fix, ax = plt.subplots()

ax.plot(n, rn)
ax.plot(n, sn)
ax.plot(n, qn)
ax.legend(['R(n)', 'S(n)', 'Q(n)'])
ax.set_xlabel('n')
ax.set_ylabel('S(n) R(n) Q(n)')
ax2 = ax.twinx()
ax2.plot(n, en, color='red')
ax2.plot(n, un,color='black')
ax2.set_ylabel('E(n) U(n)')
ax2.legend(['E(n)', 'U(n)'])
plt.show()