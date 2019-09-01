package com.alekseysamoylov.thread.creation.example

import java.util.concurrent.ThreadLocalRandom


fun main() {
    val metrics = Metrics()
    val business1 = BusinessLogic(metrics)
    val business2 = BusinessLogic(metrics)
    val metricsPrinter = MetricsPrinter(metrics)

    business1.start()
    business2.start()
    metricsPrinter.start()

    Thread.sleep(10000)
}

class MetricsPrinter(private val metrics: Metrics) : Thread() {
    @Override
    override fun run() {
        while (true) {
            Thread.sleep(2000)
            println(metrics.getAverage())

        }
    }

}

class BusinessLogic(private val metrics: Metrics) : Thread() {
    @Override
    override fun run() {
        while (true) {
            val start = System.currentTimeMillis()
            Thread.sleep(ThreadLocalRandom.current().nextLong(10))
            val end = System.currentTimeMillis()
            val sample = end - start
            metrics.addSample(sample)
        }
    }
}

class Metrics(
    private var count: Long = 0L,
    @Volatile private var average: Double = 0.0
) {
    @Synchronized
    fun addSample(sample: Long) {
        val currentSum = average * count
        count++
        average = (currentSum + sample) / count
    }

    fun getAverage(): Double = average
}
