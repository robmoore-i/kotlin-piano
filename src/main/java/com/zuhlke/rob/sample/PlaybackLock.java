package com.zuhlke.rob.sample;

import java.util.concurrent.Callable;

public class PlaybackLock {
    public void complete() {
        try {
            notifyAll();
        } catch (IllegalMonitorStateException ignored) {
        }
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
