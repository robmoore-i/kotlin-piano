package com.zuhlke.rob;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

class ClipLineListener implements LineListener {
    boolean completed = false;

    @Override
    public void update(LineEvent event) {
        LineEvent.Type lineEventType = event.getType();

        if (lineEventType == LineEvent.Type.START) {
            System.out.println("Started playback");
        }

        if (lineEventType == LineEvent.Type.STOP) {
            System.out.println("Stopped playback");
            completed = true;
            notifyAll();
        }
    }
}
