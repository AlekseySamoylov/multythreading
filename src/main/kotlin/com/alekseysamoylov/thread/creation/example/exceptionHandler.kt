package com.alekseysamoylov.thread.creation.example



fun main() {
    val threadThrowsException = Thread {
        throw RuntimeException("Thread was interrupted")
    }
    threadThrowsException.name = "New Worker"

    threadThrowsException.setUncaughtExceptionHandler { thread, exception ->
        println("Logger[error] thread: ${thread.name} exception ${exception.message}")
    }
    threadThrowsException.start()
}
