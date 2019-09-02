package com.alekseysamoylov.thread.creation.example

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

fun main() {
    val criticalBusinessClass = CriticalBusinessClass()
    val thread = Thread { criticalBusinessClass.proceedWithInterruption() }
    thread.name = "first thread"
    thread.start()
    val lockedThreadToInterrupt = Thread { criticalBusinessClass.proceedWithInterruption() }
    lockedThreadToInterrupt.start()
    lockedThreadToInterrupt.name = "locked thread to interrupt"

    Thread.sleep(4000)

    lockedThreadToInterrupt.interrupt()
}

class CriticalBusinessClass {

    private val lock: Lock = ReentrantLock(/*true*/)

    fun proceedWithInterruption() {
        try {
            println("try to do some important stuff ${Thread.currentThread().name}")
//            lock.lock()
            lock.lockInterruptibly()
            while (true) {
                println("Do some important stuff ${Thread.currentThread().name}")
                Thread.sleep(2000)
            }
        } catch (interruptedException: InterruptedException) {
            println("Thread has been interrupted ${Thread.currentThread().name}")
        } finally {
            println("Clean resources and unlock ${Thread.currentThread().name}")
            try {
                lock.unlock()
            } catch (ex: IllegalMonitorStateException) {
                println("Can't unlock because thread has been interrupted ${Thread.currentThread().name}")
            }
        }
    }

    fun proceedWithTryLock() {
        try {
            println("try to do some important stuff ${Thread.currentThread().name}")
             if (lock.tryLock()) {
                 while (true) {
                     println("Do some important stuff ${Thread.currentThread().name}")
                     Thread.sleep(2000)
                 }
             } else {
                 println("Do another important stuff ${Thread.currentThread().name}")
                 Thread.sleep(2000)
             }
        } catch (interruptedException: InterruptedException) {
            println("Thread has been interrupted ${Thread.currentThread().name}")
        } finally {
            println("Clean resources and unlock ${Thread.currentThread().name}")
            try {
                lock.unlock()
            } catch (ex: IllegalMonitorStateException) {
                println("Can't unlock because thread has been interrupted ${Thread.currentThread().name}")
            }
        }
    }

    fun proceedWithTryLockTime() {
        try {
            println("try to do some important stuff ${Thread.currentThread().name}")
             if (lock.tryLock(2, TimeUnit.SECONDS)) {
                 while (true) {
                     println("Do some important stuff ${Thread.currentThread().name}")
                     Thread.sleep(2000)
                 }
             } else {
                 println("Do another important stuff ${Thread.currentThread().name}")
                 Thread.sleep(2000)
             }
        } catch (interruptedException: InterruptedException) {
            println("Thread has been interrupted ${Thread.currentThread().name}")
        } finally {
            println("Clean resources and unlock ${Thread.currentThread().name}")
            try {
                lock.unlock()
            } catch (ex: IllegalMonitorStateException) {
                println("Can't unlock because thread has been interrupted ${Thread.currentThread().name}")
            }
        }
    }
}


