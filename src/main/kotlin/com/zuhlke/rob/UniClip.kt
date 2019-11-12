package com.zuhlke.rob

import java.time.Duration
import java.util.*
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

typealias RawClip = javax.sound.sampled.Clip

class UniClip(private val audioInputStream: AudioInputStream, private val clip: RawClip) : LineListener {
    private var complete: Boolean = false
    private val stopCallbacks: MutableList<() -> Unit> = mutableListOf()
    private val startCallbacks: MutableList<() -> Unit> = mutableListOf()

    fun playInBackground() {
        play()
    }

    fun playInForeground(lock: Lock) {
        addStopAction { lock.release() }
        play()
        lock.block { this.isComplete() }
    }

    override fun update(event: LineEvent) {
        if (event.type == LineEvent.Type.STOP) {
            stop()
        } else if (event.type == LineEvent.Type.START) {
            start()
        }
    }

    fun isComplete(): Boolean {
        return complete
    }

    private fun addStartAction(callback: () -> Unit) {
        startCallbacks.add(callback)
    }

    private fun start() {
        startCallbacks.forEach { it.invoke() }
    }

    fun addStopAction(callback: () -> Unit) {
        stopCallbacks.add(callback)
    }

    fun stop() {
        clip.close()
        audioInputStream.close()
        stopCallbacks.forEach { it.invoke() }
        complete = true
    }

    private fun play() {
        if (complete) {
            throw RuntimeException("Clip is already completed")
        }
        clip.addLineListener(this)
        clip.open(audioInputStream)
        clip.start()
    }

    fun withTimer(timer: Timer, duration: Duration): UniClip {
        addStartAction { timer.schedule(duration.toMillis()) { stop() } }
        return this
    }
}

private fun Timer.schedule(millis: Long, function: () -> Unit) {
    schedule(object : TimerTask() {
        override fun run() {
            function.invoke()
        }
    }, millis)
}
