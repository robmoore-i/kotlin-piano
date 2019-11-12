package com.zuhlke.rob

import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

class UniClipPlayer : LineListener, ClipPlayer<UniClip, Lock> {
    private val callbacks: MutableList<() -> Unit> = mutableListOf()
    private lateinit var clip: UniClip
    private lateinit var lock: Lock

    override fun play(clip: UniClip, mutex: Lock) {
        this.lock = mutex
        playInBackground(clip)
        mutex.block { clip.isComplete() }
    }

    override fun playInBackground(clip: UniClip) {
        this.clip = clip
        clip.playUsing(this)
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