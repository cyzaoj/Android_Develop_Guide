package com.aboust.develop_guide.widget.onloader

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.aboust.develop_guide.widget.onloader.callback.Callback
import com.aboust.develop_guide.widget.onloader.callback.SuccessCallback


class LoadService<T> constructor(private val converter: Converter<T>?, targetContext: TargetContext, onReloadListener: Callback.OnReloadListener?) {

    private var loadLayout: LoadLayout

    init {
        val context: Context = targetContext.context
        val originView: View = targetContext.originView
        val originLayoutParams: ViewGroup.LayoutParams = originView.layoutParams
        loadLayout = LoadLayout(context, onReloadListener)
        loadLayout.setupSuccessLayout(SuccessCallback(originView, context, onReloadListener))
        targetContext.parentView?.addView(loadLayout, targetContext.childIndex, originLayoutParams)
    }

    /**
     *
     */
    fun loadCallback(builder: OnLoader.Builder): LoadService<T> {
        val callbacks: List<Callback>? = builder.getCallbacks()
        val defaultCallback: Class<out Callback>? = builder.getDefaultCallback()
        callbacks?.let {
            for (callback in it) {
                loadLayout.setupCallback(callback)
            }
        }
        defaultCallback?.let { loadLayout.showCallback(it) }
        return this
    }


    fun showSuccess() = loadLayout.showCallback(SuccessCallback::class.java)


    fun showCallback(callback: Class<out Callback?>?) {
        loadLayout.showCallback(callback!!)
    }

    fun showWithConverter(t: T) {
        requireNotNull(converter) { "You haven't set the Converter." }
        loadLayout.showCallback(converter.map(t))
    }


    fun getLoadLayout(): LoadLayout = loadLayout

    fun getCurrentCallback(): Class<out Callback?>? = loadLayout.getCurrentCallback()


    /**
     * obtain rootView if you want keep the toolbar in Fragment
     */
    @Deprecated("obtain rootView if you want keep the toolbar in Fragment")
    fun getTitleLoadLayout(context: Context?, rootView: ViewGroup, titleView: View?): LinearLayout? {
        val newRootView = LinearLayout(context)
        newRootView.orientation = LinearLayout.VERTICAL
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        newRootView.layoutParams = layoutParams
        rootView.removeView(titleView)
        newRootView.addView(titleView)
        newRootView.addView(loadLayout, layoutParams)
        return newRootView
    }


    /**
     * modify the callback dynamically
     *
     * @param callback  which callback you want modify(layout, event)
     * @param transport a interface include modify logic
     */
    fun callback(callback: Class<out Callback?>, transport: Transport?): LoadService<T> {
        loadLayout.setCallback(callback, transport)
        return this
    }
}