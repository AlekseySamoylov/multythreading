package com.alekseysamoylov.thread.creation.example


fun main() {
    val thread = Thread {
        println("We are now in thread ${Thread.currentThread().name}")
    }

    thread.name = "New Worker"

    thread.priority = Thread.MAX_PRIORITY
    println("We are in thread ${Thread.currentThread().name}")
    thread.start()
    println("We are in thread ${Thread.currentThread().name}")

    Thread.sleep(1000)
}
