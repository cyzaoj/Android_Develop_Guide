package com.aboust.develop_guide.kit.initjob

import java.util.*


/**
 * 有向无环图的拓扑排序算法
 */
class Graph(
//顶点数
        private val verticeCount: Int) {
    //邻接表
    private val vector: Array<MutableList<Int>?> = arrayOfNulls(verticeCount)

    /**
     * 添加边
     *
     * @param u from
     * @param v to
     */
    fun addEdge(u: Int, v: Int) {
        vector[u]?.add(v)
    }

    /**
     * 拓扑排序
     */
    fun topologicalSort(): Vector<Int> {
        val indegree = IntArray(verticeCount)
        for (i in 0 until verticeCount) { //初始化所有点的入度数量
            vector[i]?.let {
                for (node in it) {
                    ++indegree[node]
                }
            }

        }
        val queue: Queue<Int> = LinkedList()
        for (i in 0 until verticeCount) { //找出所有入度为0的点
            if (0 == indegree[i]) {
                queue.add(i)
            }
        }
        var cnt = 0
        val topOrder = Vector<Int>()
        while (queue.isNotEmpty()) {
            val u: Int = queue.poll()!!
            topOrder.add(u)
            val intList = vector[u] ?: emptyList<Int>()
            for (node in intList) { //找到该点（入度为0）的所有邻接点
                val t = --indegree[node]
                if (0 == t) { //把这个点的入度减一，如果入度变成了0，那么添加到入度0的队列里
                    queue.add(node)
                }
            }
            cnt++
        }
        //检查是否有环，理论上拿出来的点的次数和点的数量应该一致，如果不一致，说明有环
        check(cnt == verticeCount) { "Exists a cycle in the graph" }
        return topOrder
    }

    init {
        for (i in 0 until verticeCount) {
            vector[i] = arrayListOf()
        }
    }
}