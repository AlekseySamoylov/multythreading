package com.alekseysamoylov.thread.creation.example

import java.util.concurrent.ThreadLocalRandom


fun main() {
    val dataSender = NotifyByObjectWaitAndNotifyWhenEnoughDataSender()
    val senderThread = Thread {
        while (true) {
            dataSender.sendData()
        }
    }
    val producerThread = Thread {
        while (true) {
            dataSender.fillData(ThreadLocalRandom.current().nextInt(50).toString())
            Thread.sleep(100)
        }
    }

    senderThread.isDaemon = true
    senderThread.start()
    producerThread.isDaemon = true
    producerThread.start()

    Thread.sleep(100000)

}

class NotifyByObjectWaitAndNotifyWhenEnoughDataSender {
    private val objectLock = Object()
    private var data: String = ""

    fun sendData() {
        synchronized(objectLock) {
            while (data.length < 100) {
                objectLock.wait()
            }
            println("Data has been sent $data")
            data = ""
        }
    }

    fun fillData(input: String) {
        synchronized(objectLock) {
            data += input
            if (data.length % 10 == 0) {
                println("Current Data length is ${data.length}")
            }
            objectLock.notifyAll()
        }
    }
}
