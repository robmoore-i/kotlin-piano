package com.zuhlke.rob.sample

import com.zuhlke.rob.logger.Logger
import javax.sound.sampled.LineEvent

abstract class BaseClipPlayer(playbackLock: PlaybackLock) : ClipPlayer {
    private val logger: Logger = Logger(this.javaClass.name)
    private var lock: PlaybackLock = playbackLock
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
                logger.v("OPEN")
                onClipOpen()
            }
            lineEventType === LineEvent.Type.CLOSE -> {
                logger.v("CLOSE")
                onClipClose()
            }
            lineEventType === LineEvent.Type.START -> {
                logger.v("START")
                onClipStart()
            }
            lineEventType === LineEvent.Type.STOP -> {
                logger.v("STOP")
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