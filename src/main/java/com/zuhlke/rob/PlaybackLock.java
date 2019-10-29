package com.zuhlke.rob;

import java.util.concurrent.Callable;

public class PlaybackLock {
    public void complete() {
        notifyAll();
    }

    public void blockUntil(Callable<Boolean> completionCheck) {
        while (true) {
            try {
                if (completionCheck.call()) break;
            } catch (Exception ignored) {
                throw new RuntimeException("Completion check failed while waiting for playback lock");
            }
            try {
                wait();
            } catch (IllegalMonitorStateException | InterruptedException ignored) {
            }
        }
    }
}
