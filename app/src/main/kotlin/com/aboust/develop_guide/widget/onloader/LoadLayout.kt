package com.aboust.develop_guide.widget.onloader

import android.content.Context
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import com.aboust.develop_guide.widget.onloader.callback.Callback
import com.aboust.develop_guide.widget.onloader.callback.SuccessCallback

class LoadLayout constructor(context: Context, private val onReloadListener: Callback.OnReloadListener?) : FrameLayout(context) {

    private val callbacks: MutableMap<Class<out Callback>, Callback> = HashMap()

    private var preCallback: Class<out Callback?>? = null
    private var curCallback: Class<out Callback?>? = null


    companion object {
        const val CALLBACK_CUSTOM_INDEX = 1
    }


    fun setupSuccessLayout(callback: Callback) {
        addCallback(callback)
        val successView: View = callback.rootView!!
        successView.visibility = View.GONE
        addView(successView)
        curCallback = SuccessCallback::class.java
    }


    fun setupCallback(callback: Callback) {
        val cloneCallback: Callback? = callback.copy()
        cloneCallback?.setCallback(null, context, onReloadListener)
        addCallback(cloneCallback!!)
    }

    fun addCallback(callback: Callback) {
        val clazz: Class<Callback> = callback.javaClass
        if (!callbacks.containsKey(clazz)) callbacks[clazz] = callback
    }


    fun showCallback(callback: Class<out Callback?>) {
        checkCallbackExist(callback)
        if (isMainThread()) {
            showCallbackView(callback)
        } else {
            postToMainThread(callback)
        }
    }

    fun getCurrentCallback(): Class<out Callback?>? = curCallback

    private fun postToMainThread(status: Class<out Callback?>) {
        post { showCallbackView(status) }
    }

    private fun showCallbackView(status: Class<out Callback?>) {
        preCallback?.let {
            if (it == status) return
            callbacks[it]?.onDetach()
        }
        if (childCount > 1) removeViewAt(CALLBACK_CUSTOM_INDEX)
        val callbackMap = callbacks.filter { it.key === status }
        if (callbackMap.isEmpty()) return
        val (key, value) = callbackMap.iterator().next()
        if (key === status) {
            val successCallback = callbacks[SuccessCallback::class.java] as SuccessCallback
            if (key == SuccessCallback::class.java) {
                successCallback.show()
            } else {
                successCallback.showWithCallback(value.getSuccessVisible())
                val rootView: View? = value.rootView()
                addView(rootView)
                value.onAttach(context, rootView)
            }
            preCallback = status
        }
        curCallback = status
    }

    fun setCallback(callback: Class<out Callback?>, transport: Transport?) = transport?.let {
        checkCallbackExist(callback)
        it.order(context, callbacks[callback]!!.obtainRootView())
    }

    private fun checkCallbackExist(callback: Class<out Callback?>) {
        require(callbacks.containsKey(callback)) {
            String.format("The Callback (%s) is nonexistent.", callback.simpleName)
        }
    }
}

fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

