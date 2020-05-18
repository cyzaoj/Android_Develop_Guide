package com.aboust.develop_guide.widget.initjob

import android.content.Context
import android.os.Looper
import androidx.annotation.UiThread
import timber.log.Timber
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


open class JobLaunch private constructor() {

    private var startTime: Long = 0

    private val futures: MutableList<Future<*>> = ArrayList()

    /**
     * 任务类型
     */
    private val jobClassTypes: MutableList<Class<out Job>> = ArrayList()

    /**
     * 所有任务
     */
    private var allJobs: MutableList<Job> = ArrayList()

    /**
     * 调用了await的时候还没结束的且需要等待的Job
     */
    private val needWaitJobs: MutableList<Job> = ArrayList()

    @Volatile
    private var onMainJobs: MutableList<Job> = ArrayList()

    @Volatile
    private var finishedJobs: MutableList<Class<out Job>> = ArrayList(100) //已经结束了的Task

    private var countDownLatch: CountDownLatch? = null

    private var dependedMap: HashMap<Class<out Job>, ArrayList<Job>> = HashMap()

//    /**
//     * 启动器分析的次数，统计下分析的耗时；
//     */
//    private val analyseCount: AtomicInteger = AtomicInteger()

    /**
     * 保存需要Wait的Job的数量
     */
    private val needWaitCount: AtomicInteger = AtomicInteger()

    fun add(job: Job?): JobLaunch {
        job?.let {
            collectDepends(it)
            allJobs.add(it)
            jobClassTypes.add(it.javaClass)
            // 非主线程且需要wait的，主线程不需要CountDownLatch也是同步的
            if (ifNeedWait(it)) {
                needWaitJobs.add(it)
                needWaitCount.getAndIncrement()
            }
        }
        return this
    }

    fun execute(job: Job) {
        if (ifNeedWait(job)) {
            needWaitCount.getAndIncrement()
        }
        job.runOn()?.execute(DispatchRunnable(job, this))
    }

    @UiThread
    fun await() {
        try {
            val value = needWaitCount.get()
            Timber.i("still has %s", value)
            for (job in needWaitJobs) {
                Timber.i("needWait: %s", job.javaClass.simpleName)
            }
            if (value > 0) countDownLatch?.await(WAIT_TIME, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            Timber.e(e)
        }
    }

    fun cancel() {
        for (future in futures) {
            future.cancel(true)
        }
    }


    private fun collectDepends(job: Job) {
        job.dependsOn()?.let {
            if (it.isNotEmpty()) {
                for (cls in it) {
                    val deps = dependedMap[cls]
                    if (null == deps) this.dependedMap[cls] = ArrayList()
                    dependedMap[cls]?.add(job)
                    if (finishedJobs.contains(cls)) job.satisfy()
                }
            }
        }

    }

    private fun ifNeedWait(job: Job): Boolean = !job.onMainThread() && job.needWait()

    @UiThread
    fun start() {
        this.startTime = System.currentTimeMillis()
        if (!onMain()) throw  RuntimeException("must be called from UiThread")

        if (allJobs.isNotEmpty()) {
//            analyseCount.getAndIncrement()
            printDependedMsg()
            allJobs = JobSort.getSortResult(allJobs, jobClassTypes)
            countDownLatch = CountDownLatch(needWaitCount.get())

            sendAndExecuteAsync()
            Timber.i(" job analyse cost %s ", System.currentTimeMillis() - startTime)
            executeMain()
        }

    }


    private fun executeMain() {
        startTime = System.currentTimeMillis()
        for (job in onMainJobs) {
            val time = System.currentTimeMillis()
            DispatchRunnable(job, this).run()
            Timber.i("real main %s cost  %s ", job.javaClass.simpleName, System.currentTimeMillis() - time)
        }
        Timber.i("mainJob cost %s", System.currentTimeMillis() - startTime)
    }

    private fun sendAndExecuteAsync() {
        for (job in allJobs) {
            if (!isMainProcess && job.onlyInMainProcess()) {
                markJobDone(job)
            } else {
                sendJobReal(job)
            }
            job.setSend(true)
        }
    }


    /**
     * 通知Children一个前置任务已完成
     *
     * @param launchJob
     */
    fun satisfyChildren(launchJob: Job) {
        val arrayList = dependedMap[launchJob.javaClass]
        if (true == arrayList?.isNotEmpty())
            for (job in arrayList) {
                job.satisfy()
            }
    }

    fun markJobDone(job: Job) {
        if (ifNeedWait(job)) {
            finishedJobs.add(job.javaClass)
            needWaitJobs.remove(job)
            countDownLatch?.countDown()
            needWaitCount.getAndDecrement()
        }
    }

    private fun sendJobReal(job: Job) {
        if (job.onMainThread()) {
            onMainJobs.add(job)
            if (job.needCall()) {
                job.call(object : JobCall {
                    override fun call() {
                        job.setFinished(true)
                        satisfyChildren(job)
                        markJobDone(job)
                        Timber.i("%s finish", job.javaClass.simpleName)
                    }
                })
            }
        } else {
            // 直接发，是否执行取决于具体线程池
            val future = job.runOn()?.submit(DispatchRunnable(job, this))
            future?.let { futures.add(it) }
        }
    }


    /**
     * 查看被依赖的信息
     */
    private fun printDependedMsg() {
        Timber.i("needWait size : %s", needWaitCount.get())
        for (cls in dependedMap.keys) {
            Timber.i("class %s    %s", cls.simpleName, dependedMap[cls]?.size)
            for (task in dependedMap[cls]!!) {
                Timber.i("class      %s ", task.javaClass.simpleName)
            }
        }
    }


    companion object {
        private const val WAIT_TIME = 10L
        private fun onMain() = Looper.getMainLooper() == Looper.myLooper()

        @Volatile
        private var hasInit = false

        private var context: Context? = null
        private var isMainProcess = false


        fun init(context: Context?) {
            context?.let {
                JobLaunch.context = context.applicationContext
                isMainProcess = onMain()
                hasInit = true
            }
        }


        fun newInstance(): JobLaunch {
            if (!hasInit) throw java.lang.RuntimeException("must call AppStarter.init first")
            return JobLaunch()
        }

    }


}