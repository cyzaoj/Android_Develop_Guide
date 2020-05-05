package com.aboust.develop_guide.widget.applaunch


import androidx.collection.ArraySet
import timber.log.Timber


object JobSort {
    private val S_NEW_TASKS_HIGH: MutableList<Job> = ArrayList() // 高优先级的Task

    /**
     * 任务的有向无环图的拓扑排序
     *
     */
    @Synchronized
    fun getSortResult(originJobs: List<Job>, clsLaunchTasks: List<Class<out Job>>): ArrayList<Job> {
        val makeTime = System.currentTimeMillis()
        val dependSet: MutableSet<Int> = ArraySet()
        val graph = Graph(originJobs.size)
        for (i in originJobs.indices) {
            val task = originJobs[i]
            val dependsOn = task.dependsOn()
            if (task.isSend() || dependsOn == null || dependsOn.isEmpty())
                continue

            for (cls in dependsOn) {
                val indexOfDepend = getIndexOfTask(originJobs, clsLaunchTasks, cls)
                check(indexOfDepend >= 0) {
                    task.javaClass.simpleName + " depends on " + cls.simpleName + " can not be found in task list "
                }
                dependSet.add(indexOfDepend)
                graph.addEdge(indexOfDepend, i)
            }
        }
        val indexList: List<Int> = graph.topologicalSort()
        val newTasksAll = getResultTasks(originJobs, dependSet, indexList)
        Timber.i("task analyse cost makeTime %s", (System.currentTimeMillis() - makeTime))
        printAllTaskName(newTasksAll)
        return newTasksAll
    }

    private fun getResultTasks(originJobs: List<Job>, dependSet: Set<Int>, indexList: List<Int>): ArrayList<Job> {
        val newTasksAll: ArrayList<Job> = ArrayList(originJobs.size)
        val newTasksDepended: MutableList<Job> = ArrayList() // 被别人依赖的
        val newTasksWithOutDepend: MutableList<Job> = ArrayList() // 没有依赖的
        val newTasksRunAsSoon: MutableList<Job> = ArrayList() // 需要提升自己优先级的，先执行（这个先是相对于没有依赖的先）
        for (index in indexList) {
            if (dependSet.contains(index)) {
                newTasksDepended.add(originJobs[index])
            } else {
                val task = originJobs[index]
                if (task.needRunAsSoon()) {
                    newTasksRunAsSoon.add(task)
                } else {
                    newTasksWithOutDepend.add(task)
                }
            }
        }
        // 顺序：被别人依赖的————》需要提升自己优先级的————》需要被等待的————》没有依赖的
        S_NEW_TASKS_HIGH.addAll(newTasksDepended)
        S_NEW_TASKS_HIGH.addAll(newTasksRunAsSoon)
        newTasksAll.addAll(S_NEW_TASKS_HIGH)
        newTasksAll.addAll(newTasksWithOutDepend)
        return newTasksAll
    }

    private fun printAllTaskName(newTasksAll: List<Job>) {
        for (task in newTasksAll) {
            Timber.i(task.javaClass.simpleName)
        }
    }

    val tasksHigh: List<Job>
        get() = S_NEW_TASKS_HIGH

    /**
     * 获取任务在任务列表中的index
     *
     * @param originJobs
     * @param
     */
    private fun getIndexOfTask(originJobs: List<Job>,
                               clsLaunchTasks: List<Class<out Job>>, cls: Class<*>?): Int {
        val index = clsLaunchTasks.indexOf(cls)
        if (0 <= index) return index

        // 仅仅是保护性代码
        val size = originJobs.size
        for (i in 0 until size) {
            if (cls!!.simpleName == originJobs[i].javaClass.simpleName) return i

        }
        return index
    }
}