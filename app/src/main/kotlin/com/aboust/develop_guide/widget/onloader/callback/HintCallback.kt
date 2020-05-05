package com.aboust.develop_guide.widget.onloader.callback

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes


class HintCallback constructor(builder: Builder) : Callback() {
    private var title: String? = null
    private var subTitle: String? = null
    private var imgResId = 0
    private var titleStyleRes = 0
    private var subTitleStyleRes = 0

    init {
        title = builder.title
        subTitle = builder.subTitle
        imgResId = builder.imgResId
        subTitleStyleRes = builder.subTitleStyleRes
        titleStyleRes = builder.titleStyleRes
    }

    override fun onCreateView(): Int {
        return 0
    }

    override fun onBuildView(context: Context?): View? {
        return LinearLayout(context)
    }

    override fun onViewCreate(context: Context?, view: View?) {
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER
        val ll = view as LinearLayout
        ll.orientation = LinearLayout.VERTICAL
        ll.gravity = Gravity.CENTER
        if (imgResId != -1) {
            val ivImage = ImageView(context)
            ivImage.setBackgroundResource(imgResId)
            ll.addView(ivImage, lp)
        }
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
                tvSubtitle.setTextAppearance(context, android.R.style.TextAppearance_Small)
            } else {
                tvSubtitle.setTextAppearance(context, subTitleStyleRes)
            }
            ll.addView(tvSubtitle, lp)
        }
    }


    open class Builder {
        var title: String? = null
        var subTitle: String? = null
        var imgResId = -1
        var subTitleStyleRes = -1
        var titleStyleRes = -1


        fun hintImage(@DrawableRes imgResId: Int): Builder {
            this.imgResId = imgResId
            return this
        }

        fun title(title: String?): Builder = title(title, -1)

        open fun title(title: String?, @StyleRes titleStyleRes: Int): Builder {
            this.title = title
            this.titleStyleRes = titleStyleRes
            return this
        }

        fun subTitle(subTitle: String?): Builder = subTitle(subTitle, -1)


        open fun subTitle(subTitle: String?, @StyleRes subTitleStyleRes: Int): Builder {
            this.subTitle = subTitle
            this.subTitleStyleRes = subTitleStyleRes
            return this
        }

        fun build(): HintCallback = HintCallback(this)

    }
}