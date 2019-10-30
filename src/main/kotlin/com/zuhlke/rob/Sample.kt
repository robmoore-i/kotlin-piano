package com.zuhlke.rob

import java.time.Duration

interface Sample {
    fun play()
    
    fun play(duration: Duration)
}