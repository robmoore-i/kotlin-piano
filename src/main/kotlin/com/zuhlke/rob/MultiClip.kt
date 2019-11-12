package com.zuhlke.rob

class MultiClip(private vararg val subclips: UniClip) {
    fun playInBackground() {
        subclips.forEach { it.playInBackground() }
    }

    fun playInForeground(mutex: Semaphore) {
        mutex.increment(subclips.size)
        subclips.forEach {
            it.addStopAction { mutex.decrement(1) }
            it.playInBackground()
        }
        mutex.block()
    }


    fun stop() {
        subclips.forEach { it.stop() }
    }

    fun isComplete(): Boolean {
        return subclips.all { it.isComplete() }
    }
}