package com.aboust.develop_guide.kit.initjob


import android.os.Looper
import android.os.Process
import androidx.core.os.TraceCompat
import timber.log.Timber


/**
 * 任务真正执行的地方
 */
class DispatchRunnable constructor(private val job: Job) : Runnable {
    private lateinit var launcher: JobLaunch

    constructor(job: Job, dispatcher: JobLaunch) : this(job) {
        launcher = dispatcher
    }

    override fun run() {
        TraceCompat.beginSection(job.javaClass.simpleName)
        Process.setThreadPriority(job.priority())
        var startTime = System.currentTimeMillis()
        job.setWaiting(true)
        job.waitToSatisfy()
        val waitTime = System.currentTimeMillis() - startTime
        startTime = System.currentTimeMillis()
        // 执行job
        job.setRunning(true)
        job.run()
        // 执行job的尾部任务
        val tailRunnable = job.getTailRunnable()
        tailRunnable?.run()
        if (!job.needCall() || !job.onMainThread()) {
            printLog(startTime, waitTime)
            job.setFinished(true)
            launcher.satisfyChildren(job)
            launcher.markJobDone(job)
            Timber.i("%s finish", job.javaClass.simpleName)
        }
        TraceCompat.endSection()
    }

    /**
     * 打印出来Job执行的日志
     *
     * @param startTime
     * @param waitTime
     */
    private fun printLog(startTime: Long, waitTime: Long) {
        val runTime = System.currentTimeMillis() - startTime
        val isMainProcess = Looper.getMainLooper() == Looper.myLooper()
        val currentThread = Thread.currentThread()
        val id = currentThread.id
        val name = currentThread.name
        Timber.i("  wait %s run  %s isMain %s  needWait %s  ThreadId %s ThreadName %s ",
                waitTime, runTime, isMainProcess,
                (job.needWait() || isMainProcess),
                id,
                name)
    }
}