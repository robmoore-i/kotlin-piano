package com.zuhlke.rob

import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

typealias RawClip = javax.sound.sampled.Clip

class UniClip(private val audioInputStream: AudioInputStream, private val clip: RawClip) : Clip<UniClipPlayer> {
    private var complete: Boolean = false

    override fun cardinality() = 1

    override fun playUsing(player: UniClipPlayer) {
        if (complete) {
            throw RuntimeException("Clip is already completed")
        }
        clip.addLineListener(player)
        clip.open(audioInputStream)
        clip.start()
    }

    override fun stop() {
        clip.close()
        audioInputStream.close()
        complete = true
    }

    override fun isComplete(): Boolean {
        return complete
    }

    fun playInBackground(player: UniClipPlayer) {
        if (complete) {
            throw RuntimeException("Clip is already completed")
        }
        clip.addLineListener(player)
        clip.open(audioInputStream)
        clip.start()
    }
}

class UniClipPlayer : LineListener, ClipPlayer<UniClip, Lock> {
    private val callbacks: MutableList<() -> Unit> = mutableListOf()
    private lateinit var clip: UniClip
    private lateinit var lock: Lock

    override fun play(clip: UniClip, mutex: Lock) {
        this.lock = mutex
        this.clip = clip
        clip.playUsing(this)
        mutex.block { clip.isComplete() }
    }

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