package com.zuhlke.rob.sample

import java.time.Duration

class PianoNoteSample(private val audioClip: AudioClip) : Sample {
    override fun play() {
        playWith(FullClipPlayer())
    }

    override fun play(duration: Duration) {
        playWith(CutoffClipPlayer(duration))
    }

    fun playWith(clipPlayer: ClipPlayer) {
        audioClip.addPlayer(clipPlayer)
        audioClip.open()
        val playbackLock: PlaybackLock = clipPlayer.playbackLock()
        audioClip.start()
        playbackLock.blockUntil { clipPlayer.isCompleted() }
        audioClip.close()
    }
}
