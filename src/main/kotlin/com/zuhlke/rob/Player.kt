package com.zuhlke.rob

import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

interface Player : LineListener {
    fun play(clip: Clip)
}

class FullPlayer(private val lock: Lock) : Player {
    private lateinit var clip: Clip

    override fun update(event: LineEvent?) {
        if (event == null) {
            return
        }
        if (event.type == LineEvent.Type.STOP) {
            onStop()
        }
    }

    override fun play(clip: Clip) {
        this.clip = clip
        clip.startWithPlaybackListener(this)
        lock.block { clip.isComplete() }
    }

    private fun onStop() {
        clip.stop()
        lock.release()
    }
}