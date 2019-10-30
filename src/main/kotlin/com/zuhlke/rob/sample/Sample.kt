package com.zuhlke.rob.sample

import java.time.Duration

interface Sample {
    fun play()
    
    fun play(duration: Duration)
}