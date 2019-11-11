package com.zuhlke.rob;

public interface Semaphore {
    static Semaphore primitiveSemaphore() {
        return new Semaphore() {
            private final Object lock = new Object();
            private volatile int count;

            @Override
            public void increment(int n) {
                synchronized (lock) {
                    count += n;
                }
            }

            @Override
            public void decrement(int n) {
                synchronized (lock) {
                    count -= n;
                    if (count <= 0) {
                        try {
                            count = 0;
                            notifyAll();
                        } catch (IllegalMonitorStateException ignored) {
                        }
                    }
                }
            }

            @Override
            public void block() {
                while (count > 0) {
                    try {
                        wait();
                    } catch (IllegalMonitorStateException | InterruptedException ignored) {
                    }
                }
            }
        };
    }

    void increment(int n);

    void decrement(int n);

    void block();
}
