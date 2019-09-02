package com.alekseysamoylov.thread.creation.example



fun main() {
    val thread = NewThread()

    thread.name = "New Worker"

    thread.priority = Thread.MAX_PRIORITY
    println("We are in thread ${Thread.currentThread().name}")
    thread.start()
    println("We are in thread ${Thread.currentThread().name}")

    Thread.sleep(1000)
}

private class NewThread : Thread() {
    override fun run() {
        println("We are now in thread $name")
        println("Priority $priority")
    }
}
