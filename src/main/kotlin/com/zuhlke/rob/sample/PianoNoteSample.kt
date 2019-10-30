package com.zuhlke.rob.sample

import java.time.Duration

class PianoNoteSample(private val clip: AudioClip) : Sample {
    override fun play() {
        playWith(FullClipPlayer())
    }

    override fun play(duration: Duration) {
        playWith(CutoffClipPlayer(duration))
    }

    private fun playWith(clipPlayer: ClipPlayer) {
        clip.addPlayer(clipPlayer)
        clip.open()
        val playbackLock: PlaybackLock = clipPlayer.playbackLock()
        clip.start()
        playbackLock.blockUntil { clipPlayer.isCompleted() }
        clip.close()
    }
}
