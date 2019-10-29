package com.zuhlke.rob

import java.time.Duration

class CutoffClipPlayer(private val duration: Duration) : BaseClipPlayer() {
    override fun onClipStart() {
        // Start a timer which calls the superclass method complete after
        //   the time specified by the field duration has elapsed.
    }

    override fun onClipStop() {
        complete();
    }
}
