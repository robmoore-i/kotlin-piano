package com.zuhlke.rob

import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

interface ClipPlayer : LineListener {
    fun play(clip: Clip)
    fun addStopAction(callback: () -> Unit)
}

abstract class SingleClipPlayer : ClipPlayer {
    abstract fun onLineEvent(event: LineEvent)

    override fun update(event: LineEvent?) {
        if (event != null) {
            onLineEvent(event)
        }
    }
}

class FullSingleClipPlayer(private val lock: Lock) : SingleClipPlayer() {
    private val callbacks: MutableList<() -> Unit> = mutableListOf()
    private lateinit var clip: Clip

    override fun addStopAction(callback: () -> Unit) {
        callbacks.add(callback)
    }

    override fun onLineEvent(event: LineEvent) {
        if (event.type == LineEvent.Type.STOP) {
            onStop()
        }
    }

    override fun play(clip: Clip) {
        this.clip = clip
        clip.playUsing(this)
        lock.block { clip.isComplete() }
    }

    private fun onStop() {
        callbacks.forEach { it.invoke() }
        clip.stop()
        lock.release()
    }
}