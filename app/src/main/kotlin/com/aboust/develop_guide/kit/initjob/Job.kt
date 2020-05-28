package com.aboust.develop_guide.kit.initjob

import android.os.Process
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService


abstract class Job : InitialJob {
    @Volatile
    private var isWaiting = false // 是否正在等待
    @Volatile
    private var isRunning = false // 是否正在执行
    @Volatile
    private var isFinished = false // Task是否执行完成

    @Volatile
    private var isSend = false// Task是否已经被分发


    /**
     * 当前Task依赖的Task数量（需要等待被依赖的Task执行完毕才能执行自己），默认没有依赖
     */
    private val depends: CountDownLatch = CountDownLatch(this.dependsOn()?.size ?: 0)


    /**
     * 当前Task等待，让依赖的Task先执行
     */
    open fun waitToSatisfy() {
        try {
            depends.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * 依赖的Task执行完一个
     */
    open fun satisfy() = depends.countDown()


    /**
     * 是否需要尽快执行，解决特殊场景的问题：一个Task耗时非常多但是优先级却一般，很有可能开始的时间较晚，
     * 导致最后只是在等它，这种可以早开始。
     *
     */
    open fun needRunAsSoon(): Boolean = false

    /**
     * Task的优先级，运行在主线程则不要去改优先级
     *
     */
    override fun priority(): Int = Process.THREAD_PRIORITY_BACKGROUND

    /**
     * Task执行在哪个线程池，默认在IO的线程池；
     * CPU 密集型的一定要切换到DispatcherExecutor.getCPUExecutor();
     *
     */
    override fun runOn(): ExecutorService? = DispatcherExecutor.iOExecutor


    /**
     * 异步线程执行的Task是否需要在被调用await的时候等待，默认不需要
     *
     */
    override fun needWait(): Boolean = false

    /**
     * 当前Task依赖的Task集合（需要等待被依赖的Task执行完毕才能执行自己），默认没有依赖
     *
     */
    override fun dependsOn(): List<Class<out Job>>? = null

    override fun onMainThread(): Boolean = false

    override fun getTailRunnable(): Runnable? = null

    override fun call(call: JobCall?) {}

    override fun needCall(): Boolean = false

    /**
     * 是否只在主进程，默认是
     *
     */
    override fun onlyInMainProcess(): Boolean = true


    open fun isSend(): Boolean = isSend
    open fun isRunning(): Boolean = isRunning
    open fun isWaiting(): Boolean = isWaiting
    open fun isFinished(): Boolean = isFinished


    open fun setSend(send: Boolean) {
        isSend = send
    }


    open fun setRunning(mIsRunning: Boolean) {
        this.isRunning = mIsRunning
    }


    open fun setWaiting(mIsWaiting: Boolean) {
        this.isWaiting = mIsWaiting
    }


    open fun setFinished(finished: Boolean) {
        isFinished = finished
    }
}