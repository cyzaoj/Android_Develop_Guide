package com.aboust.develop_guide.widget.onloader.callback

import android.content.Context
import android.view.View
import java.io.*

abstract class Callback(var rootView: View?, var context: Context?, var onReloadListener: OnReloadListener?) : Serializable {

    private var successViewVisible = false

    constructor() : this(null, null, null)


    open fun rootView(): View? {
        val resId = onCreateView()
        if (0 == resId && null !== rootView) return rootView
        if (null != onBuildView(context)) rootView = onBuildView(context)
        if (null == rootView) rootView = View.inflate(context, onCreateView(), null)
        rootView!!.setOnClickListener(View.OnClickListener { v ->
            if (onReloadEvent(context, rootView)) return@OnClickListener
            onReloadListener?.onReload(v)
        })
        onViewCreate(context, rootView)
        return rootView
    }

    protected open fun onBuildView(context: Context?): View? = null


    open fun setCallback(view: View?, context: Context?, onReloadListener: OnReloadListener?): Callback {
        rootView = view
        this.context = context
        this.onReloadListener = onReloadListener
        return this
    }

    /**
     * if return true, the successView will be visible when the view of callback is attached.
     */
    open fun getSuccessVisible(): Boolean = successViewVisible

    open fun setSuccessVisible(visible: Boolean) {
        successViewVisible = visible
    }


    @Deprecated("Use {@link #onReloadEvent(Context context, View view)} instead.", ReplaceWith("false"))
    protected open fun onRetry(context: Context?, view: View?): Boolean = false

    protected open fun onReloadEvent(context: Context?, view: View?): Boolean = false

    open fun obtainRootView(): View? {
        if (null == this.rootView) this.rootView = View.inflate(context, onCreateView(), null)
        return rootView
    }

    protected abstract fun onCreateView(): Int

    /**
     * Called immediately after [.onCreateView]
     */
    protected open fun onViewCreate(context: Context?, view: View?) {}

    /**
     * Called when the rootView of Callback is attached to its LoadLayout.
     *
     */
    open fun onAttach(context: Context?, view: View?) {}

    /**
     * Called when the rootView of Callback is removed from its LoadLayout.
     */
    open fun onDetach() {}


    open fun copy(): Callback? {
        val bao = ByteArrayOutputStream()
        val oos: ObjectOutputStream
        var obj: Any? = null
        try {
            oos = ObjectOutputStream(bao)
            oos.writeObject(this)
            oos.close()
            val bis = ByteArrayInputStream(bao.toByteArray())
            val ois = ObjectInputStream(bis)
            obj = ois.readObject()
            ois.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj as Callback?
    }


    interface OnReloadListener : Serializable {
        fun onReload(v: View?)
    }
}
