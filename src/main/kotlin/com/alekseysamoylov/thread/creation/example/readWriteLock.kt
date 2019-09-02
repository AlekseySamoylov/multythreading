package com.alekseysamoylov.thread.creation.example

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.locks.ReentrantReadWriteLock


fun main() {
    val cacheString = ReadWriteCache<String>()
    cacheString.put(1, "Hello world")

    val readThread = Thread { while (true) println(cacheString.get(1)) }
    val writeThread = Thread {
        while (true) {
            Thread.sleep(2000)
            cacheString.put(1, "Hello ${ThreadLocalRandom.current().nextInt(10)}")
        }
    }
    readThread.start()
    writeThread.isDaemon = true
    writeThread.start()
}

class ReadWriteCache<T> {
    private val readWriteLock = ReentrantReadWriteLock()
    private val cache = ConcurrentHashMap<Long, T>()

    fun get(id: Long): T? {
        try {
            readWriteLock.readLock().lock()
            Thread.sleep(500)
            return cache[id]
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    fun put(id: Long, value: T) {
        try {
            readWriteLock.writeLock().lockInterruptibly()
            Thread.sleep(2000)
            cache[id] = value
        } finally {
            readWriteLock.writeLock().unlock()
        }
    }

    fun evictCache() {
        try {
            readWriteLock.writeLock().lockInterruptibly()
            Thread.sleep(3000)
            cache.clear()
        } finally {
            readWriteLock.writeLock().unlock()
        }
    }
}
