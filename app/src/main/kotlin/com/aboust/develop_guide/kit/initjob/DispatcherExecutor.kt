package com.aboust.develop_guide.kit.initjob

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min


object DispatcherExecutor {

    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private val CORE_POOL_SIZE = max(2, min(CPU_COUNT - 1, 5))
    private val MAXIMUM_POOL_SIZE = CORE_POOL_SIZE
    private const val KEEP_ALIVE_SECONDS = 5L


    private var sCPUThreadPoolExecutor: ThreadPoolExecutor? = null
    private var sIOThreadPoolExecutor: ExecutorService? = null

    private val factory = DefaultThreadFactory()
    private val workQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
    private val handler: RejectedExecutionHandler = RejectedExecutionHandler { r, _ ->
        // 一般不会到这里
        Executors.newCachedThreadPool().execute(r)
    }

    /**
     * 获取CPU线程池
     */
    val cPUExecutor: ThreadPoolExecutor?
        get() = sCPUThreadPoolExecutor

    /**
     * 获取IO线程池
     */
    val iOExecutor: ExecutorService?
        get() = sIOThreadPoolExecutor

    private class DefaultThreadFactory internal constructor() : ThreadFactory {
        private val group: ThreadGroup?
        private val threadNumber: AtomicInteger = AtomicInteger(1)
        private val namePrefix: String

        companion object {
            private val poolNumber: AtomicInteger = AtomicInteger(1)
        }

        override fun newThread(r: Runnable?): Thread {
            val t = Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0)
            if (t.isDaemon) t.isDaemon = false
            if (t.priority != Thread.NORM_PRIORITY) t.priority = Thread.NORM_PRIORITY
            return t
        }


        init {
            val s = System.getSecurityManager()
            val thread = Thread.currentThread()
            group = if (null != s) s.threadGroup else thread.threadGroup
            namePrefix = "TaskDispatcherPool-${poolNumber.getAndIncrement()}-Thread-"
        }
    }

    init {
        sCPUThreadPoolExecutor = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS, workQueue, factory, handler)
        sCPUThreadPoolExecutor?.allowCoreThreadTimeOut(true)
        sIOThreadPoolExecutor = Executors.newCachedThreadPool(factory)
    }
}