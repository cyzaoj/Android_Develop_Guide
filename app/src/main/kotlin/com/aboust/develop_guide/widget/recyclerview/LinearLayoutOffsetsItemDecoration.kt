package com.aboust.develop_guide.widget.recyclerview


import android.graphics.Rect
import android.util.SparseArray
import android.view.View
import androidx.annotation.IntDef
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


/**
 * This class can only be used in the RecyclerView which use a LinearLayoutManager or
 * its subclass.
 */
class LinearLayoutOffsetsItemDecoration(@field:Orientation private var mOrientation: Int) : ItemDecoration() {
    private val mTypeOffsetsFactories = SparseArray<OffsetsCreator>()

    @IntDef(LINEAR_OFFSETS_HORIZONTAL, LINEAR_OFFSETS_VERTICAL)
    @Retention(AnnotationRetention.SOURCE)
    private annotation class Orientation

    private var mItemOffsets = 0
    private var mIsOffsetEdge = true
    private var mIsOffsetLast = true
    fun orientation(@Orientation orientation: Int) {
        mOrientation = orientation
    }

    fun itemOffsets(itemOffsets: Int) {
        mItemOffsets = itemOffsets
    }

    fun offsetEdge(isOffsetEdge: Boolean) {
        mIsOffsetEdge = isOffsetEdge
    }

    fun offsetLast(isOffsetLast: Boolean) {
        mIsOffsetLast = isOffsetLast
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val adapterPosition = parent.getChildAdapterPosition(view)
        if (mOrientation == LINEAR_OFFSETS_HORIZONTAL) {
            outRect.right = getDividerOffsets(parent, view)
        } else {
            outRect.bottom = getDividerOffsets(parent, view)
        }
        if (mIsOffsetEdge) {
            if (mOrientation == LINEAR_OFFSETS_HORIZONTAL) {
                outRect.left = if (adapterPosition == 0) outRect.right else 0
                outRect.top = outRect.right
                outRect.bottom = outRect.right
            } else {
                outRect.top = if (adapterPosition == 0) outRect.bottom else 0
                outRect.left = outRect.bottom
                outRect.right = outRect.bottom
            }
        }
        if (adapterPosition == parent.adapter!!.itemCount - 1 && !mIsOffsetLast) {
            if (mOrientation == LINEAR_OFFSETS_HORIZONTAL) {
                outRect.right = 0
            } else {
                outRect.bottom = 0
            }
        }
    }

    private fun getDividerOffsets(parent: RecyclerView, view: View): Int {
        if (mTypeOffsetsFactories.size() == 0) {
            return mItemOffsets
        }
        val adapterPosition = parent.getChildAdapterPosition(view)
        val itemType = parent.adapter!!.getItemViewType(adapterPosition)
        val offsetsCreator = mTypeOffsetsFactories[itemType]
        return offsetsCreator?.create(parent, adapterPosition) ?: 0
    }

    fun registerTypeOffset(itemType: Int, offsetsCreator: OffsetsCreator) {
        mTypeOffsetsFactories.put(itemType, offsetsCreator)
    }

    interface OffsetsCreator {
        fun create(parent: RecyclerView?, adapterPosition: Int): Int
    }

    companion object {
        const val LINEAR_OFFSETS_HORIZONTAL = LinearLayoutManager.HORIZONTAL
        const val LINEAR_OFFSETS_VERTICAL = LinearLayoutManager.VERTICAL
    }

}