package com.alekseysamoylov.thread.creation.example


class InterruptedWorker(private val toggle: Toggle) : Runnable {

    override fun run() {

        while (!toggle.isInterrupted()) {
            println("Work in progress")
            Thread.sleep(1000)
        }
    }
}

class Toggle {
    @Volatile
    private var interrupt = false

    fun stop() {
        interrupt = true
    }

    fun isInterrupted() = interrupt
}

fun main() {
    val toggle = Toggle()
    val thread = Thread(InterruptedWorker(toggle))

    thread.start()
    Thread.sleep(2000)

    println("Try to interrupt")
    thread.interrupt()
    Thread.sleep(2000)
    println("Set daemon true")
    thread.isDaemon = true
    thread.interrupt()
    Thread.sleep(3000)

    toggle.stop()
}
