package com.zuhlke.rob

import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

abstract class UniClipPlayer : LineListener {
    abstract fun play(clip: UniClip, lock: Lock)
    abstract fun playInBackground(clip: UniClip)
    abstract fun addStopAction(callback: () -> Unit)
    abstract fun onLineEvent(event: LineEvent)

    override fun update(event: LineEvent?) {
        if (event != null) {
            onLineEvent(event)
        }
    }
}

class FullUniClipPlayer : UniClipPlayer() {
    private val callbacks: MutableList<() -> Unit> = mutableListOf()
    private lateinit var clip: UniClip
    private lateinit var lock: Lock

    override fun addStopAction(callback: () -> Unit) {
        callbacks.add(callback)
    }

    override fun onLineEvent(event: LineEvent) {
        if (event.type == LineEvent.Type.STOP) {
            onStop()
        }
    }

    override fun play(clip: UniClip, lock: Lock) {
        this.lock = lock
        playInBackground(clip)
        lock.block { clip.isComplete() }
    }

    override fun playInBackground(clip: UniClip) {
        this.clip = clip
        clip.playUsing(this)
    }

    private fun onStop() {
        callbacks.forEach { it.invoke() }
        clip.stop()
        lock.release()
    }
}