package com.alekseysamoylov.thread.creation.example


fun main() {
    val sharedClass = SharedClass()

    Thread {while (true) sharedClass.increment()}.start()
    Thread {while (true) sharedClass.checkWrongOrder()}.start()
}



class SharedClass(
//    @Volatile
    var x: Long = 0,
//    @Volatile
    var y: Long = 0
) {
    fun increment() {
        x++
        y++
    }

    fun checkWrongOrder() {
        if (y > x) {
            println("Wrong order")
        }
    }
}
