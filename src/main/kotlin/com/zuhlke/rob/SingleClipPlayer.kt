package com.zuhlke.rob

import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

abstract class SingleClipPlayer : LineListener {
    abstract fun play(clip: SingleClip, lock: Lock)
    abstract fun playInBackground(clip: SingleClip)
    abstract fun addStopAction(callback: () -> Unit)
    abstract fun onLineEvent(event: LineEvent)

    override fun update(event: LineEvent?) {
        if (event != null) {
            onLineEvent(event)
        }
    }
}

class FullSingleClipPlayer() : SingleClipPlayer() {
    private val callbacks: MutableList<() -> Unit> = mutableListOf()
    private lateinit var clip: SingleClip
    private lateinit var lock: Lock

    override fun addStopAction(callback: () -> Unit) {
        callbacks.add(callback)
    }

    override fun onLineEvent(event: LineEvent) {
        if (event.type == LineEvent.Type.STOP) {
            onStop()
        }
    }

    override fun play(clip: SingleClip, lock: Lock) {
        this.lock = lock
        playInBackground(clip)
        lock.block { clip.isComplete() }
    }

    override fun playInBackground(clip: SingleClip) {
        this.clip = clip
        clip.playUsing(this)
    }

    private fun onStop() {
        callbacks.forEach { it.invoke() }
        clip.stop()
        lock.release()
    }
}