package com.aboust.develop_guide.widget.applaunch

import android.os.Looper
import android.os.MessageQueue.IdleHandler
import java.util.*


class DelayInitDispatcher {
    private val delayJobs: Queue<Job> = LinkedList()
    private val idleHandler = IdleHandler {
        if (delayJobs.isNotEmpty()) {
            val job = delayJobs.poll()
            job?.let { DispatchRunnable(it).run() }
        }
        !delayJobs.isEmpty()
    }

    fun add(job: Job): DelayInitDispatcher {
        delayJobs.add(job)
        return this
    }

    fun start() {
        Looper.myQueue().addIdleHandler(idleHandler)
    }
}