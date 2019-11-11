package com.zuhlke.rob;

public interface Semaphore {
    void increment(int n);

    void decrement(int n);

    void block();
}
