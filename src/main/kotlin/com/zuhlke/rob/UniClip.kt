package com.zuhlke.rob

import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

typealias RawClip = javax.sound.sampled.Clip

class UniClip(private val audioInputStream: AudioInputStream, private val clip: RawClip) : Clip<UniClip>, LineListener {
    private lateinit var lock: Lock
    private var complete: Boolean = false
    private val callbacks: MutableList<() -> Unit> = mutableListOf()

    override fun cardinality() = 1

    override fun playUsing(player: UniClip) {
        if (complete) {
            throw RuntimeException("Clip is already completed")
        }
        clip.addLineListener(this)
        clip.open(audioInputStream)
        clip.start()
    }

    override fun stop() {
        clip.close()
        audioInputStream.close()
        complete = true
        callbacks.forEach { it.invoke() }
        lock.release()
    }

    override fun isComplete(): Boolean {
        return complete
    }

    override fun update(event: LineEvent) {
        if (event.type == LineEvent.Type.STOP) {
            stop()
        }
    }

    fun playInBackground(player: UniClip) {
        playUsing(player)
    }

    fun play(lock: Lock, player: UniClip) {
        this.lock = lock
        playUsing(player)
        lock.block { this.isComplete() }
    }

    fun addStopAction(callback: () -> Unit) {
        callbacks.add(callback)
    }
}

class UniClipPlayer : LineListener {
    private val callbacks: MutableList<() -> Unit> = mutableListOf()
    private lateinit var clip: UniClip
    private lateinit var lock: Lock

    override fun update(event: LineEvent) {
        if (event.type == LineEvent.Type.STOP) {
            onStop()
        }
    }

    fun addStopAction(callback: () -> Unit) {
        callbacks.add(callback)
    }

    private fun onStop() {
        callbacks.forEach { it.invoke() }
        clip.stop()
        lock.release()
    }
}