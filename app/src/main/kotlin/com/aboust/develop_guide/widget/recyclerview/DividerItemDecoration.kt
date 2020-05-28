package com.aboust.develop_guide.widget.recyclerview

import android.annotation.TargetApi
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class DividerItemDecoration(orientation: Int) : ItemDecoration() {

    private var divider: Drawable? = null
    private var orientation = HORIZONTAL
    private val bounds: Rect = Rect()

    private var mShowHeaderDivider = false
    private var mShowFooterDivider = true


    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    init {
        orientation(orientation)
    }

    fun orientation(orientation: Int) {
        require(!(orientation != 0 && orientation != 1)) { "Invalid orientation. It should be either HORIZONTAL or VERTICAL" }
        this.orientation = orientation
    }

    fun setDrawable(drawable: Drawable) {
        divider = drawable
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager != null && divider != null) {
            if (orientation == 1) {
                drawVertical(c, parent)
            } else {
                drawHorizontal(c, parent)
            }
        }
    }


    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            val top = parent.paddingTop
            val bottom = parent.height - parent.paddingBottom
            canvas.clipRect(left, top, right, bottom)
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            var top: Int
            var bottom: Int
            if (i == 0 && mShowHeaderDivider) {
                top = 0
                divider!!.setBounds(left, top, right, top + divider!!.intrinsicHeight)
                divider!!.draw(canvas)
            }
            if (i != childCount - 1 || mShowFooterDivider) {
                bottom = bounds.bottom + Math.round(child.getTranslationY())
                top = bottom - divider!!.intrinsicHeight
                divider!!.setBounds(left, top, right, bottom)
                divider!!.draw(canvas)
            }
        }
        canvas.restore()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
        } else {
            top = 0
            bottom = parent.height
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            parent.layoutManager!!.getDecoratedBoundsWithMargins(child, bounds)
            val right: Int = bounds.right + Math.round(child.getTranslationX())
            val left = right - divider!!.intrinsicWidth
            divider!!.setBounds(left, top, right, bottom)
            divider!!.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (divider == null) {
            outRect.set(0, 0, 0, 0)
        } else {
            if (orientation == 1) {
                outRect.set(0, 0, 0, divider!!.intrinsicHeight)
            } else {
                outRect.set(0, 0, divider!!.intrinsicWidth, 0)
            }
        }
    }

    fun showHeaderDivider(show: Boolean) {
        mShowHeaderDivider = show
    }

    fun showFooterDivider(show: Boolean) {
        mShowFooterDivider = show
    }


}