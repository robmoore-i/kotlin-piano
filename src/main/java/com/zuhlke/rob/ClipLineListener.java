package com.zuhlke.rob;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

class ClipLineListener implements LineListener {
    private Object lock;
    private boolean completed = false;

    @Override
    public void update(LineEvent event) {
        LineEvent.Type lineEventType = event.getType();
        if (lineEventType == LineEvent.Type.STOP) {
            completed = true;
            lock.notifyAll();
        }
    }

    public boolean isCompleted() {
        return completed;
    }

    public java.lang.Object playbackLock() {
        Object lock = new Object();
        this.lock = lock;
        return lock;
    }
}
