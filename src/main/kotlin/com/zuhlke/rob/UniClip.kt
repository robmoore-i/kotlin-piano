package com.zuhlke.rob

import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

typealias RawClip = javax.sound.sampled.Clip

class UniClip(private val audioInputStream: AudioInputStream, private val clip: RawClip) : LineListener {
    private var complete: Boolean = false
    private val callbacks: MutableList<() -> Unit> = mutableListOf()

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
        }
    }

    fun isComplete(): Boolean {
        return complete
    }

    fun addStopAction(callback: () -> Unit) {
        callbacks.add(callback)
    }

    fun stop() {
        clip.close()
        audioInputStream.close()
        callbacks.forEach { it.invoke() }
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
}