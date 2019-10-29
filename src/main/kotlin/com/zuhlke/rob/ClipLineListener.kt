package com.zuhlke.rob;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

class ClipLineListener implements LineListener {
    private PlaybackLock lock;
    private boolean completed = false;

    @Override
    public void update(LineEvent event) {
        LineEvent.Type lineEventType = event.getType();
        if (lineEventType == LineEvent.Type.STOP) {
            completed = true;
            lock.complete();
        }
    }

    public boolean isCompleted() {
        return completed;
    }

    public PlaybackLock playbackLock() {
        PlaybackLock lock = new PlaybackLock();
        this.lock = lock;
        return lock;
    }
}
