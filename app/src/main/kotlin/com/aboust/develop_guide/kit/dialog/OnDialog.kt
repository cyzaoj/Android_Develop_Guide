package com.aboust.develop_guide.kit.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.SparseArray
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.aboust.develop_guide.R
import kotlin.properties.Delegates


/**
 * 通用dialog
 *
 *
 *
val dialog = OnDialog(this)
dialog.layout(R.layout.dialog_confirm).with()  //必须
//1.设置宽
.width() //单位:dp
.widthPX() //单位:px
.widthRatio(0.8) //占屏幕宽比例

//2.设置高
.height() //单位:dp
.heightPX() //单位:px
.heightRatio() //占屏幕高比例

//3.设置背景
.backgroundColor(Color.WHITE) //背景颜色
.backgroundColorRes(R.color.white) //res资源
.backgroundRadius() //圆角, 单位：dp
.backgroundRadiusPx() //圆角, 单位：px

//4.设置弹框位置 和 动画(显示和隐藏动画)
.gravity(Gravity.TOP and Gravity.BOTTOM) //设置弹框位置
.gravity(Gravity.LEFT, 0, 0) //设置弹框位置(偏移量)
.windowAnimations(R.style.dialog_translate) //设置动画

//5.设置具体布局
//5.1 常见系统View属性
.text(R.id.tv_title, "确定")
.textColor()
.background()
.backgroundRes()
.bitmap()
.visible()
.gone()
//5.2 其它属性
.cancel(R.id.tv_cancel) //设置按钮，弹框消失的按钮
.onClickListener(object : DialogOnClickListener {
override fun onClick(view: View?, lDialog: OnDialog?) {
}

}, R.id.tv_confirm, R.id.tv_cancel)  //可以传多个
.show(); //显示
 *
 *
 */
class OnDialog : Dialog {

    private var layoutRes by Delegates.notNull<Int>()
    private val views: SparseArray<View> = SparseArray()
    private var width = 0
    private var height = 0
    private var bgRadius = 0 //背景圆角

    @ColorInt
    private var backgroundColor: Int = Color.WHITE //背景颜色


    constructor(context: Context) : super(context)
    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId)

    constructor(@NonNull context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener)

    fun layout(@LayoutRes layoutRes: Int): OnDialog {
        this.layoutRes = layoutRes
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(getRoundRectDrawable(dp2px(context, 5F), Color.WHITE))
        this.width = (getWidthPixels(context) * 0.8).toInt()
        this.height = WindowManager.LayoutParams.WRAP_CONTENT
        fixSize()
        window?.setWindowAnimations(R.style.dialog_alpha)
    }


    fun with(): OnDialog {
        show()
        dismiss()
        return this
    }


    //    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置动画>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    fun windowAnimations(@StyleRes style: Int): OnDialog {
        window?.setWindowAnimations(style)
        return this
    }

//    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置位置>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    /**
     * 设置位置
     * @param gravity
     * @param offX
     * @param offY
     */
    fun gravity(gravity: Int, offX: Int, offY: Int): OnDialog {
        val layoutParams = window?.attributes
        layoutParams?.x = offX
        layoutParams?.y = offY
        window?.attributes = layoutParams
        return gravity(gravity)

    }

    fun gravity(gravity: Int): OnDialog {
        window?.setGravity(gravity)
        return this
    }

    //    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置背景>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private fun background(): OnDialog {
        window?.setBackgroundDrawable(getRoundRectDrawable(bgRadius, backgroundColor))
        return this
    }

    fun backgroundColor(@ColorInt color: Int): OnDialog {
        backgroundColor = color
        return background()
    }


    fun backgroundColorRes(@ColorRes colorRes: Int): OnDialog {
//        bgColor = context.resources.getColor(colorRes)
        backgroundColor = ContextCompat.getColor(this.context, colorRes)
        return background()
    }


    /**
     *  设置背景圆角
     * @param bgRadius
     */
    public fun backgroundRadius(bgRadius: Int): OnDialog {
        this.bgRadius = dp2px(context, bgRadius.toFloat())
        return background()
    }

    public fun backgroundRadiusPx(bgRadius: Int): OnDialog {
        this.bgRadius = bgRadius
        return background()
    }

    //    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置宽高>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    /**
     * 设置宽高
     */
    private fun fixSize(): OnDialog {
        val dialogWindow: Window? = window
        val lp: WindowManager.LayoutParams? = dialogWindow?.attributes
        lp?.width = width
        lp?.height = height
        dialogWindow?.attributes = lp
        return this
    }

    fun width(width: Int): OnDialog {
        this.width = dp2px(context, width.toFloat())
        return fixSize()
    }

    fun widthPX(width: Int): OnDialog {
        this.width = width
        return fixSize()
    }

    fun height(height: Int): OnDialog {
        this.height = dp2px(context, height.toFloat())
        return fixSize()
    }

    fun heightPX(height: Int): OnDialog {
        this.height = height
        return fixSize()
    }


    /**
     * 设置宽占屏幕的比例
     * @param widthRatio
     */
    fun widthRatio(widthRatio: Double): OnDialog {
        width = (getWidthPixels(context) * widthRatio).toInt()
        fixSize()
        return this
    }

    /**
     * 设置高占屏幕的比例
     * @param heightRatio
     */
    fun heightRatio(heightRatio: Double): OnDialog {
        height = (getHeightPixels(context) * heightRatio).toInt()
        fixSize()
        return this
    }

    //    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    /**
     * 设置监听
     * @param onClickListener
     * @param viewIds
     */
    fun onClickListener(onClickListener: DialogOnClickListener, vararg viewIds: Int): OnDialog {
        val lDialog: OnDialog = this
        for (element in viewIds) {
            val el = getView<View>(element)
            el?.setOnClickListener { v -> onClickListener.onClick(v, lDialog) }
        }
        return this
    }

    /**
     * 设置 关闭dialog的按钮
     * @param viewId
     * @return
     */
    fun cancel(@IdRes viewId: Int): OnDialog {
        getView<View>(viewId)?.setOnClickListener { dismiss() }
        return this
    }


    //    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置常见属性>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    @SuppressWarnings("unchecked")
    private fun <T : View?> getView(@IdRes viewId: Int): T? {
        var view: View? = views[viewId]
        if (view == null) {
            view = findViewById(viewId)
            views.put(viewId, view)
        }
        return view as T?
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The BaseViewHolder for chaining.
     */
    fun text(@IdRes viewId: Int, value: CharSequence?): OnDialog {
        val view = getView<TextView>(viewId)!!
        view.text = value
        return this
    }

    fun text(@IdRes viewId: Int, @StringRes strId: Int): OnDialog {
        val view = getView<TextView>(viewId)!!
        view.setText(strId)
        return this
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The BaseViewHolder for chaining.
     */
    fun imageResource(@IdRes viewId: Int, @DrawableRes imageResId: Int): OnDialog {
        val view: ImageView? = getView<ImageView>(viewId)
        view?.setImageResource(imageResId)
        return this
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The BaseViewHolder for chaining.
     */
    fun backgroundColor(@IdRes viewId: Int, @ColorInt color: Int): OnDialog {
        val view = getView<View>(viewId)!!
        view.setBackgroundColor(color)
        return this
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The BaseViewHolder for chaining.
     */
    fun backgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): OnDialog {
        val view = getView<View>(viewId)!!
        view.setBackgroundResource(backgroundRes)
        return this
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The BaseViewHolder for chaining.
     */
    fun textColor(@IdRes viewId: Int, @ColorInt textColor: Int): OnDialog {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(textColor)
        return this
    }


    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The BaseViewHolder for chaining.
     */
    fun imageDrawable(@IdRes viewId: Int, drawable: Drawable?): OnDialog {
        val view: ImageView? = getView(viewId)
        view?.setImageDrawable(drawable)
        return this
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    fun bitmap(@IdRes viewId: Int, bitmap: Bitmap?): OnDialog {
        val view: ImageView? = getView(viewId)
        view?.setImageBitmap(bitmap)
        return this
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    @SuppressLint("ObsoleteSdkInt")
    fun alpha(@IdRes viewId: Int, value: Float): OnDialog {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId)!!.alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getView<View>(viewId)!!.startAnimation(alpha)
        }
        return this
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The BaseViewHolder for chaining.
     */
    fun gone(@IdRes viewId: Int, visible: Boolean): OnDialog {
        val view = getView<View>(viewId)!!
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    /**
     * Set a view visibility to VISIBLE (true) or INVISIBLE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for INVISIBLE.
     * @return The BaseViewHolder for chaining.
     */
    fun visible(@IdRes viewId: Int, visible: Boolean): OnDialog {
        val view = getView<View>(viewId)!!
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        return this
    }

    private fun getRoundRectDrawable(radius: Int, @ColorInt color: Int): GradientDrawable? {
        //左上、右上、右下、左下的圆角半径
        val radiusArray = floatArrayOf(radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat())
        val drawable = GradientDrawable()
        drawable.cornerRadii = radiusArray
        drawable.setColor(if (color != 0) color else Color.TRANSPARENT)
        return drawable
    }


    interface DialogOnClickListener {
        fun onClick(view: View?, lDialog: OnDialog?)
    }


    /** 1.获取屏幕宽度(单位px)  */
    fun getWidthPixels(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /** 2.获取屏幕高度(单位px)  */
    fun getHeightPixels(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /** 3.获取屏幕密度 （0.75 / 1.0 / 1.5） 参考网址;http://blog.sina.com.cn/s/blog_74c22b210100s0kw.html  */
    fun getHeightPixels(activity: Activity): Float {
        val metric = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metric)
        return metric.density
    }

    /** 4. 获取状态栏高度 (单位px) */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 是否竖屏
     * @param context
     * @return
     */
    fun isPortrait(context: Context): Boolean {
        val mConfiguration: Configuration = context.resources.configuration //获取设置的配置信息
        return mConfiguration.orientation == ORIENTATION_PORTRAIT
    }

    /**
     * 是否横屏
     * @param context
     * @return
     */
    fun isLandscape(context: Context): Boolean {
        val mConfiguration: Configuration = context.resources.configuration //获取设置的配置信息
        return mConfiguration.orientation == ORIENTATION_LANDSCAPE
    }

    /**
     * 强制设置为竖屏
     * @param activity
     */
    fun setPortrait(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 强制设置为横屏
     * @param activity
     */
    fun setLandscape(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    fun isPad(context: Context): Boolean {
        return ((context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }


    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
    }

    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.resources.displayMetrics).toInt()
    }

    fun px2dp(context: Context, pxVal: Float): Float {
        return pxVal / context.resources.displayMetrics.density
    }

    fun px2sp(context: Context, pxVal: Float): Float {
        return pxVal / context.resources.displayMetrics.scaledDensity
    }


}