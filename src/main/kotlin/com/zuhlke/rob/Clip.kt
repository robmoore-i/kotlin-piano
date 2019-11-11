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

class MultiAudioClip(private val semaphore: Semaphore, private vararg val subclips: Clip) : Clip {
    override fun startWithPlaybackListener(player: Player) {
        semaphore.increment(subclips.size)
        subclips.forEach { player.play(it) }
    }

    override fun stop() {
    }

    override fun isComplete(): Boolean {
        return false
    }
}