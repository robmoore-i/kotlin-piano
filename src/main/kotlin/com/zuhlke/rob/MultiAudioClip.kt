package com.zuhlke.rob

interface MultiClip {
    val cardinality: Int

    fun playUsing(singleClipPlayerProvider: () -> SingleClipPlayer)
    fun stop()
    fun isComplete(): Boolean
}

class MultiAudioClip(private vararg val subclips: SingleClip) : MultiClip {
    override val cardinality: Int = subclips.size

    override fun playUsing(singleClipPlayerProvider: () -> SingleClipPlayer) {
        subclips.forEach { singleClipPlayerProvider.invoke().play(it) }
    }

    override fun stop() {
        subclips.forEach { it.stop() }
    }

    override fun isComplete(): Boolean {
        return subclips.all { it.isComplete() }
    }
}