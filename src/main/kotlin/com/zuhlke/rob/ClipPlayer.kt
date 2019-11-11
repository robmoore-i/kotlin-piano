package com.zuhlke.rob

import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

abstract class ClipPlayer : LineListener {
    abstract fun play(clip: Clip)
    abstract fun onLineEvent(event: LineEvent)

    override fun update(event: LineEvent?) {
        if (event == null) {
            return
        }
        onLineEvent(event)
    }
}

class SingleFullClipPlayer(private val lock: Lock) : ClipPlayer() {
    private lateinit var clip: Clip

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
        clip.stop()
        lock.release()
    }
}