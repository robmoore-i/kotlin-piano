package com.zuhlke.rob

class MultiClipPlayer(private val uniClipPlayerProvider: () -> UniClipPlayer) {
    fun play(mutliClip: MultiClip, semaphore: Semaphore) {
        semaphore.increment(mutliClip.cardinality())
        mutliClip.playUsing {
            val singleClipPlayer = uniClipPlayerProvider.invoke()
            singleClipPlayer.addStopAction { semaphore.decrement(1) }
            singleClipPlayer
        }
        semaphore.block()
    }
}
