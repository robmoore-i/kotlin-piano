package com.zuhlke.rob

interface ClipPlayer<Clip, Mutex> {
    fun play(clip: Clip, mutex: Mutex)
    fun playInBackground(clip: Clip)
}