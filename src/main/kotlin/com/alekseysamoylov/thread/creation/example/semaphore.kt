package com.alekseysamoylov.thread.creation.example

import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.locks.ReentrantLock
import java.util.stream.Collectors


fun main() {
    val shipmentGate = ProductionStockShipmentGate()

    val consumerThread = Thread {
        while (true) {
            println("Items that received to delivery ${shipmentGate.getAll()}")
        }
    }
    val producerThread = Thread {
        while (true) {
            val item = "Item # ${ThreadLocalRandom.current().nextInt(50)}"
            shipmentGate.putIntoStock(item)
            Thread.sleep(500)
        }
    }

    consumerThread.isDaemon = true
    consumerThread.start()
    producerThread.isDaemon = true
    producerThread.start()

    Thread.sleep(200000)
}

class ProductionStockShipmentGate {
    private val emptySemaphore = Semaphore(STOCK_CAPACITY)
    private val fullSemaphore = Semaphore(0)
    private val lock = ReentrantLock()
    private val stockQueue: Queue<String> = ArrayDeque<String>(10)

    fun putIntoStock(item: String) {
        try {
            emptySemaphore.acquire()
            lock.lock()
            stockQueue.add(item)
            println("The item $item added to the stock shipment gate")
            if (stockQueue.size == 10) {
                fullSemaphore.release()
                println("The stock shipment gate is fully equipped. It can be delivered")
            }
        } finally {
            lock.unlock()
        }
    }

    fun getAll(): List<String> {
        try {
            println("Try to get items from the stock")
            fullSemaphore.acquire()
            lock.lock()
            val result: List<String> = stockQueue.parallelStream().collect(Collectors.toList())
            stockQueue.clear()
            return result
        } finally {
            lock.unlock()
            emptySemaphore.release(STOCK_CAPACITY)
        }
    }
}

private const val STOCK_CAPACITY = 10
