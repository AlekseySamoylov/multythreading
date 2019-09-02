package com.alekseysamoylov.thread.creation.example

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock


fun main() {
    val dataSender = NotifyByConditionAndSignalWhenEnoughDataSender()
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

class NotifyByConditionAndSignalWhenEnoughDataSender {
    private val lock = ReentrantLock()
    private val condition: Condition = lock.newCondition()
    private var data: String = ""

    fun sendData() {
        lock.lock()
        try {
            while (data.length < 100) {
//                condition.awaitUninterruptibly()
//                condition.await(15, TimeUnit.SECONDS)
                condition.await()
            }
            println("Data has been sent $data")
            data = ""
        } finally {
            lock.unlock()
        }
    }

    fun fillData(input: String) {
        lock.lock()
        try {
            data += input
            if (data.length % 10 == 0) {
                println("Current Data length is ${data.length}")
            }
            condition.signal()
        } finally {
            lock.unlock()
        }
    }
}




