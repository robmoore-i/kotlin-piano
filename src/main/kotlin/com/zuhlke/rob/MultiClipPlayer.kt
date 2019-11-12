package com.zuhlke.rob

class MultiClipPlayer(private val uniClipPlayerProvider: () -> UniClipPlayer) : ClipPlayer<MultiClip, Semaphore> {
    override fun play(clip: MultiClip, mutex: Semaphore) {
        mutex.increment(clip.cardinality())
        clip.playUsing {
            val singleClipPlayer = uniClipPlayerProvider.invoke()
            singleClipPlayer.addStopAction { mutex.decrement(1) }
            singleClipPlayer
        }
        mutex.block()
    }

    override fun playInBackground(clip: MultiClip) {
        clip.playUsing(uniClipPlayerProvider)
    }
}
