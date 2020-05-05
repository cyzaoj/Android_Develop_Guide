package com.aboust.develop_guide.widget.onloader

import com.aboust.develop_guide.widget.onloader.callback.Callback


class OnLoader private constructor(var builder: Builder) {

    constructor() : this(Builder())

    companion object {
        val INSTANCE: OnLoader by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { OnLoader() }

        fun begin(): Builder {
            return Builder()
        }
    }


    private fun builder(builder: Builder) {
        this.builder = builder
    }

    fun <T> register(target: Any): LoadService<T> {
        return register(target, null, null)
    }

    fun <T> register(target: Any, onReloadListener: Callback.OnReloadListener?): LoadService<T> {
        return this.register(target, onReloadListener, null)
    }


    fun <T> register(target: Any, onReloadListener: Callback.OnReloadListener?, converter: Converter<T>?): LoadService<T> {
        val targetContext: TargetContext = getTargetContext(target)
        return LoadService(converter, targetContext, onReloadListener).loadCallback(builder)
    }


    class Builder {

        private val callbacks: MutableList<Callback> = ArrayList()

        private var defaultCallback: Class<out Callback>? = null

        fun addCallback(callback: Callback): Builder {
            callbacks.add(callback)
            return this
        }

        fun defaultCallback(defaultCallback: Class<out Callback?>): Builder {
            this.defaultCallback = defaultCallback
            return this
        }

        fun getCallbacks(): List<Callback>? = callbacks

        fun getDefaultCallback(): Class<out Callback>? = defaultCallback

        fun commit() = INSTANCE.builder(this)

        fun build(): OnLoader = OnLoader(this)
    }
}