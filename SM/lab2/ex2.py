import numpy as np
import matplotlib.pyplot as plt

# merge sort: https://www.dcc.fc.up.pt/~ricroc/aulas/1516/cp/apontamentos/slides_sorting.pdf
# T(1) = n * log(n)
# T(n) = n
# O(1) = O(n) = n

n = np.arange(1, 32, 0.1)

sn = np.divide(np.multiply(n, np.log2(n)), np.subtract(n, np.log2(n)));
en = np.divide(sn, n)
rn = np.divide(n, n)
un = np.multiply(rn, en)
qn = np.divide(np.multiply(sn, en), rn)
_, ax = plt.subplots()

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