package com.aboust.develop_guide.widget.recyclerview

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.IntDef
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *
 *
 * onDraw：在item绘制之前时被调用，将指定的内容绘制到item view内容之下；
 * onDrawOver：在item被绘制之后调用，将指定的内容绘制到item view内容之上
 * getItemOffsets：在每次测量item尺寸时被调用，将decoration的尺寸计算到item的尺寸中
 */
class DividerAlignChildViewItemDecoration(@field:Orientation var orientation: Int) : RecyclerView.ItemDecoration() {


    @IntDef(HORIZONTAL, VERTICAL)
    @Retention(AnnotationRetention.SOURCE)
    private annotation class Orientation

    private var divider: Drawable? = null
        set(value) {
            requireNotNull(value) { "Drawable cannot be null." }
            field = value
        }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (null == parent.layoutManager || divider == null) return

        when (orientation) {
            HORIZONTAL -> {
            }
            VERTICAL -> {
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
    }


    companion object {
        const val HORIZONTAL = LinearLayoutManager.HORIZONTAL
        const val VERTICAL = LinearLayoutManager.VERTICAL
    }
}