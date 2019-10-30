package com.zuhlke.rob.sample

import javax.sound.sampled.LineEvent

abstract class BaseClipPlayer : ClipPlayer {
    private var lock: PlaybackLock = PlaybackLock()
    private var completed = false

    override fun isCompleted(): Boolean {
        return completed
    }

    override fun playbackLock(): PlaybackLock {
        return lock
    }

    override fun update(event: LineEvent) {
        val lineEventType = event.type
        when {
            lineEventType === LineEvent.Type.OPEN -> {
                println("OPEN")
                onClipOpen()
            }
            lineEventType === LineEvent.Type.CLOSE -> {
                println("CLOSE")
                onClipClose()
            }
            lineEventType === LineEvent.Type.START -> {
                println("START")
                onClipStart()
            }
            lineEventType === LineEvent.Type.STOP -> {
                println("STOP")
                onClipStop()
            }
        }
    }

    fun complete() {
        completed = true
        lock.complete()
    }

    open fun onClipStart() {
    }

    open fun onClipStop() {
    }

    open fun onClipOpen() {
    }

    open fun onClipClose() {
    }
}