package com.zuhlke.rob

import javax.sound.sampled.AudioInputStream

typealias RawClip = javax.sound.sampled.Clip

interface Clip {
    fun startWithPlaybackListener(clipPlayer: ClipPlayer)
    fun stop()
    fun isComplete(): Boolean
}

class AudioClip(private val audioInputStream: AudioInputStream, private val clip: RawClip) : Clip {
    private var complete: Boolean = false

    override fun startWithPlaybackListener(clipPlayer: ClipPlayer) {
        if (complete) {
            throw RuntimeException("Clip is already completed")
        }
        clip.addLineListener(clipPlayer)
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

class MultiAudioClip(private val semaphore: Semaphore, private vararg val subclips: Clip) : Clip {
    override fun startWithPlaybackListener(clipPlayer: ClipPlayer) {
        semaphore.increment(subclips.size)
        subclips.forEach { clipPlayer.play(it) }
    }

    override fun stop() {
    }

    override fun isComplete(): Boolean {
        return false
    }
}