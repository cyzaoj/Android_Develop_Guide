package com.aboust.develop_guide.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import kotlin.math.ceil

/*
//initialize
mProgressStatusBar = new ProgressStatusBar(this);

//show progress
mProgressStatusBar.startFakeProgress(3000); //make fake progress from 0 to 100 in 3 sec.
//or
mProgressStatusBar.setProgress(60); //set progress value manually


*/
/*Addidional*//*

//options, anytime before you start a new progress
mProgressStatusBar.setProgressColor(COLOR);//default #40212121
mProgressStatusBar.setProgressBackgroundColor(COLOR);//default transparent

//Listener
mProgressStatusBar.setProgressListener(new ProgressStatusBar.OnProgressListener() {
    public void onStart() {
        //ex: lock the UI or tent it
    }
    public void onUpdate(int progress) {
        //ex: simulate with another progressView
    }
    public void onEnd() {
        //ex: continue the job
    }
});
*/


class ProgressStatusBar : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    private var windowManager: WindowManager?
    private var params: WindowManager.LayoutParams
    private var progressPaint: Paint
    private var progress = 0
    private var isViewAdded = false
    private val listener: OnProgressListener? = null


    init {
        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) "#40212121" else "#60ffffff"
        val barColor: Int = Color.parseColor(color)
        progressPaint = Paint()
        progressPaint.style = Paint.Style.FILL
        progressPaint.color = barColor
//        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager = ContextCompat.getSystemService(context, WindowManager::class.java)
        params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, getStatusBarHeight(context),
                WindowManager.LayoutParams.FIRST_SUB_WINDOW,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        or WindowManager.LayoutParams.FLAG_FULLSCREEN
                        or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT)
        params.gravity = Gravity.TOP or Gravity.START
        setBackgroundColor(Color.TRANSPARENT)
    }

    private fun remove() {
        progress = 0
        if (isViewAdded) {
            isViewAdded = false
            windowManager?.removeViewImmediate(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        remove()
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (100 != progress) {
            val progressEndX = width * progress / 100f
            canvas.drawRect(0f, top.toFloat(), progressEndX, bottom.toFloat(), progressPaint)
        }
    }


    private fun prepare() {
        if (!isViewAdded) {
            progress = 0
            windowManager?.addView(this, params)
            isViewAdded = true
        }
    }

    /**
     * duration 秒完成0 - 100
     */
    fun startFakeProgress(duration: Int) {
        prepare()
        val barProgress = ValueAnimator.ofFloat(0f, 1f)
        barProgress.duration = duration.toLong()
        barProgress.interpolator = DecelerateInterpolator()
        barProgress.addUpdateListener { animation ->
            val interpolation = animation.animatedValue as Float
            progress = (interpolation * 100).toInt()
            if (isViewAdded) {
                invalidate()
                listener?.onProgress(progress)
            }
        }
        barProgress.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                listener?.onStart()
            }

            override fun onAnimationEnd(animation: Animator) {
                listener?.onComplete()
                remove()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        barProgress.start()
    }

    fun progress(progress: Int) {
        prepare()
        if (0 == this.progress) listener?.onStart()
        this.progress = progress
        if (progress < 100) {
            invalidate()
            listener?.onProgress(progress)
        } else {
            remove()
            listener?.onComplete()
        }
    }

    interface OnProgressListener {
        fun onStart()
        fun onProgress(progress: Int)
        fun onComplete()
    }


    fun progressColor(@ColorInt color: Int) {
        progressPaint.color = color
    }

    fun progressBackgroundColor(@ColorInt color: Int) {
        setBackgroundColor(color)
    }

    private fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        result = if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            ceil((if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) 24 else 25) * context.resources.displayMetrics.density.toDouble()).toInt()
        }
        return result
    }
}



