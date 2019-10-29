package com.zuhlke.rob

import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

internal class ClipLineListener : LineListener {
    private var lock: PlaybackLock = PlaybackLock()
    var isCompleted = false
        private set

    override fun update(event: LineEvent) {
        val lineEventType = event.type
        if (lineEventType === LineEvent.Type.STOP) {
            isCompleted = true
            lock.complete()
        }
    }

    fun playbackLock(): PlaybackLock {
        return lock
    }
}
