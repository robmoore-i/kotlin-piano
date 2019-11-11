package com.zuhlke.rob

import javax.sound.sampled.AudioInputStream

typealias RawClip = javax.sound.sampled.Clip

interface SingleClip {
    fun playUsing(singleClipPlayer: SingleClipPlayer)
    fun stop()
    fun isComplete(): Boolean
}

class SingleAudioClip(private val audioInputStream: AudioInputStream, private val clip: RawClip) : SingleClip {
    private var complete: Boolean = false

    override fun playUsing(singleClipPlayer: SingleClipPlayer) {
        if (complete) {
            throw RuntimeException("Clip is already completed")
        }
        clip.addLineListener(singleClipPlayer)
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
}