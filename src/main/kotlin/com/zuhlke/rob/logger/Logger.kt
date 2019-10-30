package com.zuhlke.rob.logger

class Logger(val name: String) {
    fun v(message: String) {
        println("$name : $message")
    }
}