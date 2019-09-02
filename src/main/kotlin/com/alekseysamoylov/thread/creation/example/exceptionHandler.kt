package com.alekseysamoylov.thread.creation.example








fun main() {
    val thread = Thread {
        throw RuntimeException("Thread was interrupted")
    }
    thread.name = "New Worker"

    thread.setUncaughtExceptionHandler { thread, exception ->
        println("Logger[error] thread: ${thread.name} exception ${exception.message}")
    }
    thread.start()
}
