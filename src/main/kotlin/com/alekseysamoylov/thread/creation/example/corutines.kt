package com.alekseysamoylov.thread.creation.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() {
    val promise = GlobalScope.launch {
            delay(1000L)
            println("World")
    }
    println("Hello")
    runBlocking {
        promise.join()
    }

    runBlocking {
        while (true) {
            delay(500L)
            println("Hello world")
        }
    }
}



