package com.alekseysamoylov.thread.creation.example

import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.LockSupport


fun main() {
    val baseNode = VersionNode("Hello World")
    val atomicVersionControl = AtomicVersionControl(baseNode)

    val threadList: MutableList<Thread> = mutableListOf()

    for (i in 1 .. 10) {
        val thread = Thread {
            for (i in 1..1000) {
                val newVersion = VersionNode(atomicVersionControl.getCurrentVersion().content + "+ i " +
                        ThreadLocalRandom.current().nextInt(10))
                atomicVersionControl.addNewVersion(newVersion)
            }
        }
        threadList.add(thread)
    }

    for(thread in threadList) {
        thread.start()
    }


    for(thread in threadList) {
        thread.join()
    }

    println("Should be 10001 = ${atomicVersionControl.getCurrentVersion().getVersionCount()}")
}


class AtomicVersionControl(versionNode: VersionNode) {
    private var currentVersionAtomicReference: AtomicReference<VersionNode> = AtomicReference(versionNode)

    fun addNewVersion(newVersion: VersionNode) {
        // without check
//            val currentNode = currentVersionAtomicReference.get()
//            newVersion.previousNode = Optional.of(currentNode)
//            currentNode.nextNode = Optional.of(newVersion)
//            currentVersionAtomicReference.set(newVersion)

        while (true) {
            val currentNode = currentVersionAtomicReference.get()
            newVersion.previousNode = Optional.of(currentNode)
            currentNode.nextNode = Optional.of(newVersion)
            if (currentVersionAtomicReference.compareAndSet(currentNode, newVersion)) {
                break
            } else {
                LockSupport.parkNanos(1000)
            }
        }
    }

    fun getCurrentVersion(): VersionNode {
        return currentVersionAtomicReference.get()
    }
}


class VersionNode(var content: String = "") {
    var previousNode: Optional<VersionNode> = Optional.empty()
    var nextNode: Optional<VersionNode> = Optional.empty()

    fun getVersionCount(): Int {
        if (previousNode.isPresent) {
            return previousNode.get().getVersionCount() + 1
        } else {
            return 1
        }
    }
}
