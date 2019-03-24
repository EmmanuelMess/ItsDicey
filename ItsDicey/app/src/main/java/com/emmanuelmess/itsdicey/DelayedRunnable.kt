package com.emmanuelmess.itsdicey

import android.os.Handler
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class DelayedRunnable(delay: Long, f: () -> Unit): Future<Unit> {
    private val handler = Handler()

    private var runnableRef = Runnable {
        isDone = true
        f()
    }

    private var isCancelled = false
    private var isDone = false

    init {
        handler.postDelayed(runnableRef, delay)
    }

    override fun isDone() = isDone

    override fun get() = Unit

    override fun get(timeout: Long, unit: TimeUnit?) = Unit

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        if(mayInterruptIfRunning) {
            throw UnsupportedOperationException()
        }

        isCancelled = true
        handler.removeCallbacks(runnableRef)

        return true
    }

    override fun isCancelled() = isCancelled

}