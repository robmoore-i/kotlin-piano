package com.zuhlke.rob

interface MultiClip {
    val cardinality: Int

    fun playUsing(uniClipPlayerProvider: () -> UniClipPlayer)
    fun stop()
    fun isComplete(): Boolean
}

class MultiAudioClip(private vararg val subclips: UniClip) : MultiClip {
    override val cardinality: Int = subclips.size

    override fun playUsing(uniClipPlayerProvider: () -> UniClipPlayer) {
        subclips.forEach { uniClipPlayerProvider.invoke().playInBackground(it) }
    }

    override fun stop() {
        subclips.forEach { it.stop() }
    }

    override fun isComplete(): Boolean {
        return subclips.all { it.isComplete() }
    }
}