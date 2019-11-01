package com.zuhlke.rob

import javax.sound.sampled.AudioInputStream

typealias RawClip = javax.sound.sampled.Clip

interface Clip {
    fun startWithPlaybackListener(player: Player)
    fun stop()
    fun isComplete(): Boolean
}

class AudioClip(private val audioInputStream: AudioInputStream, private val clip: RawClip) : Clip {
    private var complete: Boolean = false

    override fun startWithPlaybackListener(player: Player) {
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
}