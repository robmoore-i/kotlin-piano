package com.zuhlke.rob.sample

import java.time.Duration
import java.util.*
import kotlin.concurrent.schedule

class CutoffClipPlayer(private val duration: Duration) : BaseClipPlayer() {
    override fun onClipStart() {
        Timer().schedule(duration.toMillis()) {
            complete()
        }
    }

    override fun onClipStop() {
        complete()
    }
}
