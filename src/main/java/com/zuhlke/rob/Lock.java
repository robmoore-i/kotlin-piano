package com.zuhlke.rob;

import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;

public interface Lock {
    static Lock primitiveLock() {
        return new Lock() {
            @Override
            public void release() {
                try {
                    notifyAll();
                } catch (IllegalMonitorStateException ignored) {
                }
            }

            @Override
            public void block(@NotNull Function0<Boolean> completionCheck) {
                while (true) {
                    try {
                        if (completionCheck.invoke()) break;
                    } catch (Exception ignored) {
                        throw new RuntimeException("Completion check failed while waiting for playback lock");
                    }
                    try {
                        wait();
                    } catch (IllegalMonitorStateException | InterruptedException ignored) {
                    }
                }
            }
        };
    }

    void release();

    void block(@NotNull Function0<Boolean> completionCheck);
}
