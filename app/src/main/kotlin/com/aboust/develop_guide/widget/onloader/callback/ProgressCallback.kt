package com.aboust.develop_guide.widget.onloader.callback

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.StyleRes


class ProgressCallback private constructor(builder: Builder) : Callback() {
    private val title: String?
    private val subTitle: String?
    private var subTitleStyleRes = -1
    private var titleStyleRes = -1
    override fun onCreateView(): Int = 0

    override fun onBuildView(context: Context?): View = LinearLayout(context)

    override fun onViewCreate(context: Context?, view: View?) {
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER
        val ll = view as LinearLayout
        ll.orientation = LinearLayout.VERTICAL
        ll.gravity = Gravity.CENTER
        val progressBar = ProgressBar(context)
        ll.addView(progressBar, lp)
        if (!TextUtils.isEmpty(title)) {
            val tvTitle = TextView(context)
            tvTitle.text = title
            if (titleStyleRes == -1) {
                tvTitle.setTextAppearance(context, android.R.style.TextAppearance_Large)
            } else {
                tvTitle.setTextAppearance(context, titleStyleRes)
            }
            ll.addView(tvTitle, lp)
        }
        if (!TextUtils.isEmpty(subTitle)) {
            val tvSubtitle = TextView(context)
            tvSubtitle.text = subTitle
            if (subTitleStyleRes == -1) {
                tvSubtitle.setTextAppearance(context, android.R.style.TextAppearance_Medium)
            } else {
                tvSubtitle.setTextAppearance(context, subTitleStyleRes)
            }
            ll.addView(tvSubtitle, lp)
        }
    }

    class Builder {
        internal var title: String? = null
        internal var subTitle: String? = null
        internal var subTitleStyleRes = -1
        internal var titleStyleRes = -1
        internal var aboveable = false
        fun title(title: String?): Builder = title(title, -1)

        fun title(title: String?, @StyleRes titleStyleRes: Int): Builder {
            this.title = title
            this.titleStyleRes = titleStyleRes
            return this
        }

        fun subTitle(subTitle: String?): Builder = subTitle(subTitle, -1)

        fun subTitle(subTitle: String?, @StyleRes subTitleStyleRes: Int): Builder {
            this.subTitle = subTitle
            this.subTitleStyleRes = subTitleStyleRes
            return this
        }

        fun aboveSuccess(above: Boolean): Builder {
            this.aboveable = above
            return this
        }

        fun build(): ProgressCallback = ProgressCallback(this)
    }

    init {
        title = builder.title
        subTitle = builder.subTitle
        subTitleStyleRes = builder.subTitleStyleRes
        titleStyleRes = builder.titleStyleRes
        setSuccessVisible(builder.aboveable)
    }
}