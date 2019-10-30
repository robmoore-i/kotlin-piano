package com.zuhlke.rob.sample

import javax.sound.sampled.LineListener

interface ClipPlayer : LineListener {
    fun playbackLock(): PlaybackLock

    fun isCompleted(): Boolean
}