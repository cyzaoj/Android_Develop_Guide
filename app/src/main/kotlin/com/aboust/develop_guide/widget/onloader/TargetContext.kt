package com.aboust.develop_guide.widget.onloader

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup


data class TargetContext(val context: Context, val parentView: ViewGroup?, val originView: View, val childIndex: Int)

fun getTargetContext(target: Any): TargetContext {
    val contentParent: ViewGroup?
    val context: Context
    when (target) {
        is Activity -> {
            context = target
            contentParent = target.findViewById<View>(android.R.id.content) as ViewGroup
        }
        is View -> {
            contentParent = target.parent as ViewGroup?
            context = target.context
        }
        else -> throw IllegalArgumentException("The target must be within Activity, Fragment, View.")
    }
    val childCount = contentParent?.childCount ?: 0
    var childIndex = 0
    val originView: View?
    if (target is View) {
        originView = target
        for (i in 0 until childCount) {
            if (originView === contentParent?.getChildAt(i)) {
                childIndex = i
                break
            }
        }
    } else {
        originView = contentParent?.getChildAt(0)
    }
    requireNotNull(originView) { String.format("unexpected error when register in %s", target.javaClass.simpleName) }
    contentParent?.removeView(originView)
    return TargetContext(context, contentParent, originView, childIndex)
}