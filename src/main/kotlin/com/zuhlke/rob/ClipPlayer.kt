package com.zuhlke.rob

import javax.sound.sampled.LineListener

interface ClipPlayer : LineListener {
    fun playbackLock(): PlaybackLock

    fun isCompleted(): Boolean
}