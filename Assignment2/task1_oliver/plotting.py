from cProfile import label
from tokenize import Double
import numpy as np
import matplotlib.pyplot as plt


def main():
    assign2_task1_p1()
    # assign2_task1_p4()


def assign2_task1_p1():
    with open("./assign_2_task_1.txt") as f:
        ys = np.fromiter(
            map(
                lambda data: int(data),
                f.read().splitlines()
            ),
            dtype=np.int64
        )

        T = 1
        M = 4000

        xs = np.linspace(T, T * M, M)
        plt.plot(xs, ys)
        plt.xlabel("Measurement Time")
        plt.ylabel("# Customers in system being served")
        plt.grid()
        plt.show()


def assign2_task1_p4():
    with open("./A2_t1_p4_CI.txt") as f:
        ys = np.fromiter(
            map(
                lambda data: np.float64(data),
                f.read().splitlines()
            ),
            dtype=np.float64
        )

        xs = np.linspace(1, 100, 100)
        plt.plot(xs, ys)
        plt.xlabel("run sim i")
        plt.ylabel("# Mean customers in system being served")
        plt.grid()
        plt.show()


if __name__ == "__main__":
    main()
