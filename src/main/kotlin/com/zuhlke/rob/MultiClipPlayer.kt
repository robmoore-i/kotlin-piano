package com.zuhlke.rob

class MultiClipPlayer(private val semaphore: Semaphore, private val singleClipPlayerProvider: () -> SingleClipPlayer) {
    fun play(mutliClip: MultiClip) {
        semaphore.increment(mutliClip.cardinality)
        mutliClip.playUsing {
            val singleClipPlayer = singleClipPlayerProvider.invoke()
            singleClipPlayer.addStopAction { semaphore.decrement(1) }
            singleClipPlayer
        }
        semaphore.block()
    }
}
