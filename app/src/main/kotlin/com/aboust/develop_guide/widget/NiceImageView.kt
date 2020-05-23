package com.aboust.develop_guide.widget


import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import com.aboust.develop_guide.R


/**
 *
 *
 *
 * 属性名	                                        含义	                                    默认值	                                对应方法
 * is_circle	                                    是否显示为圆形（默认为矩形）	            false	                                isCircle()
 * corner_top_left_radius	                        左上角圆角半径	                        0dp	                                    setCornerTopLeftRadius()
 * corner_top_right_radius	                        右上角圆角半径	                        0dp	                                    setCornerTopRightRadius()
 * corner_bottom_left_radius	                    左下角圆角半径	                        0dp	                                    setCornerBottomLeftRadius()
 * corner_bottom_right_radius	                    右下角圆角半径	                        0dp	                                    setCornerBottomRightRadius()
 * corner_radius	                                统一设置四个角的圆角半径	                0dp	                                    setCornerRadius()
 * border_width	                                    边框宽度	                                0dp	                                    setBorderWidth()
 * border_color	                                    边框颜色	                                #ffffff	                                setBorderColor()
 * inner_border_width	                            相当于内层边框（is_circle为true时支持）  	0dp	                                    setInnerBorderWidth()
 * inner_border_color	                            内边框颜色	                            #ffffff	                                setInnerBorderColor()
 * is_cover_src	border、inner_border                是否覆盖图片内容	                        false	                                isCoverSrc()
 * mask_color	                                    图片上绘制的遮罩颜色	                    不设置颜色则不绘制	                        setMaskColor()
 */
class NiceImageView : AppCompatImageView {


    /***
     * 是否显示为圆形，如果为圆形则设置的corner无效
     */
    private var isCircle = false

    /**
     * border、inner_border是否覆盖图片
     */
    private var isCoverSrc = false

    /**
     * 边框宽度
     */
    private var borderWidth = 0

    /**
     * 边框颜色
     */
    private var borderColor = Color.WHITE


    /**
     * 内层边框宽度
     */
    private var innerBorderWidth = 0

    /**
     * 内层边框充色
     */
    private var innerBorderColor = Color.WHITE
    private var cornerRadius = 0

    /**
     * 左上角圆角半径
     */
    private var cornerTopLeftRadius = 0

    /**
     * 右上角圆角半径
     */
    private var cornerTopRightRadius = 0

    /**
     * 左下角圆角半径
     */
    private var cornerBottomLeftRadius = 0

    /**
     * 右下角圆角半径
     */
    private var cornerBottomRightRadius = 0

    /**
     * 遮罩颜色
     */
    private var maskColor = 0
    private var xferMode: Xfermode? = null


    private var oWidth: Int = 0
    private var oHeight: Int = 0

    private var radius = 0F
    private lateinit var borderRadii: FloatArray
    private lateinit var srcRadii: FloatArray

    /**
     * 图片占的矩形区域
     */
    private lateinit var srcRectF: RectF

    /**
     * 边框的矩形区域
     */
    private lateinit var borderRectF: RectF
    private lateinit var paint: Paint

    /**
     * 用来裁剪图片的ptah
     */
    private lateinit var path: Path

    /**
     * 图片区域大小的path
     */
    private var srcPath: Path? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.initViews(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.initViews(attrs)
    }

    private fun initViews(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.NiceImageView, 0, 0)
        for (i in 0 until ta.indexCount) {
            when (val attr = ta.getIndex(i)) {
                R.styleable.NiceImageView_is_cover_src -> isCoverSrc = ta.getBoolean(attr, isCoverSrc)
                R.styleable.NiceImageView_is_circle -> isCircle = ta.getBoolean(attr, isCircle)
                R.styleable.NiceImageView_border_width -> borderWidth = ta.getDimensionPixelSize(attr, borderWidth)
                R.styleable.NiceImageView_border_color -> borderColor = ta.getColor(attr, borderColor)
                R.styleable.NiceImageView_inner_border_width -> innerBorderWidth = ta.getDimensionPixelSize(attr, innerBorderWidth)
                R.styleable.NiceImageView_inner_border_color -> {
                    innerBorderColor = ta.getColor(attr, innerBorderColor)
                }
                R.styleable.NiceImageView_corner_radius -> {
                    cornerRadius = ta.getDimensionPixelSize(attr, cornerRadius)
                }
                R.styleable.NiceImageView_corner_top_left_radius -> {
                    cornerTopLeftRadius = ta.getDimensionPixelSize(attr, cornerTopLeftRadius)
                }
                R.styleable.NiceImageView_corner_top_right_radius -> {
                    cornerTopRightRadius = ta.getDimensionPixelSize(attr, cornerTopRightRadius)
                }
                R.styleable.NiceImageView_corner_bottom_left_radius -> {
                    cornerBottomLeftRadius = ta.getDimensionPixelSize(attr, cornerBottomLeftRadius)
                }
                R.styleable.NiceImageView_corner_bottom_right_radius -> {
                    cornerBottomRightRadius = ta.getDimensionPixelSize(attr, cornerBottomRightRadius)
                }
                R.styleable.NiceImageView_mask_color -> {
                    maskColor = ta.getColor(attr, maskColor)
                }
            }
        }
        ta.recycle()
        borderRadii = FloatArray(8)
        srcRadii = FloatArray(8)
        borderRectF = RectF()
        srcRectF = RectF()
        paint = Paint()
        path = Path()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            xferMode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        } else {
            xferMode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            srcPath = Path()
        }
        calculateRadii()
        clearInnerBorderWidth()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        oWidth = w
        oHeight = h
        initBorderRectF()
        initSrcRectF()
    }

    override fun onDraw(canvas: Canvas) {
        // 使用图形混合模式来显示指定区域的图片
        canvas.saveLayer(srcRectF, null, Canvas.ALL_SAVE_FLAG)
        if (!isCoverSrc) {
            val sx = 1.0f * (oWidth - 2 * borderWidth - 2 * innerBorderWidth) / oWidth
            val sy = 1.0f * (oHeight - 2 * borderWidth - 2 * innerBorderWidth) / oHeight
            // 缩小画布，使图片内容不被borders覆盖
            canvas.scale(sx, sy, oWidth / 2.0f, oHeight / 2.0f)
        }
        super.onDraw(canvas)
        paint.reset()
        path.reset()
        if (isCircle) {
            path.addCircle(oWidth / 2.0f, oHeight / 2.0f, radius, Path.Direction.CCW)
        } else {
            path.addRoundRect(srcRectF, srcRadii, Path.Direction.CCW)
        }
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.xfermode = xferMode
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            canvas.drawPath(path, paint)
        } else {
            srcPath!!.addRect(srcRectF, Path.Direction.CCW)
            // 计算tempPath和path的差集
            srcPath!!.op(path, Path.Op.DIFFERENCE)
            canvas.drawPath(srcPath!!, paint)
        }
        paint.xfermode = null

        // 绘制遮罩
        if (maskColor != 0) {
            paint.color = maskColor
            canvas.drawPath(path, paint)
        }
        // 恢复画布
        canvas.restore()
        // 绘制边框
        drawBorders(canvas)
    }

    private fun drawBorders(canvas: Canvas) {
        if (isCircle) {
            if (borderWidth > 0) {
                drawCircleBorder(canvas, borderWidth, borderColor, radius - borderWidth / 2.0f)
            }
            if (innerBorderWidth > 0) {
                drawCircleBorder(canvas, innerBorderWidth, innerBorderColor, radius - borderWidth - innerBorderWidth / 2.0f)
            }
        } else {
            if (borderWidth > 0) {
                drawRectFBorder(canvas, borderWidth, borderColor, borderRectF, borderRadii)
            }
        }
    }

    private fun drawCircleBorder(canvas: Canvas, borderWidth: Int, borderColor: Int, radius: Float) {
        initBorderPaint(borderWidth, borderColor)
        path.addCircle(oWidth / 2.0f, oHeight / 2.0f, radius, Path.Direction.CCW)
        canvas.drawPath(path, paint)
    }

    private fun drawRectFBorder(canvas: Canvas, borderWidth: Int, borderColor: Int, rectF: RectF, radii: FloatArray) {
        initBorderPaint(borderWidth, borderColor)
        path.addRoundRect(rectF, radii, Path.Direction.CCW)
        canvas.drawPath(path, paint)
    }

    private fun initBorderPaint(borderWidth: Int, borderColor: Int) {
        path.reset()
        paint.strokeWidth = borderWidth.toFloat()
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
    }

    /**
     * 计算外边框的RectF
     */
    private fun initBorderRectF() {
        if (!isCircle) {
            borderRectF[borderWidth / 2.0f, borderWidth / 2.0f, oWidth - borderWidth / 2.0f] = oHeight - borderWidth / 2.0f
        }
    }

    /**
     * 计算图片原始区域的RectF
     */
    private fun initSrcRectF() {
        if (isCircle) {
            radius = oWidth.coerceAtMost(oHeight) / 2.0f
            srcRectF[oWidth / 2.0f - radius, oHeight / 2.0f - radius, oWidth / 2.0f + radius] = oHeight / 2.0f + radius
        } else {
            srcRectF[0f, 0f, oWidth.toFloat()] = oHeight.toFloat()
            if (isCoverSrc) {
                srcRectF = borderRectF
            }
        }
    }

    /**
     * 计算RectF的圆角半径
     */
    private fun calculateRadii() {
        if (isCircle) {
            return
        }
        if (cornerRadius > 0) {
            for (i in borderRadii.indices) {
                borderRadii[i] = cornerRadius.toFloat()
                srcRadii[i] = cornerRadius - borderWidth / 2.0f
            }
        } else {
            borderRadii[1] = cornerTopLeftRadius.toFloat()
            borderRadii[0] = borderRadii[1]
            borderRadii[3] = cornerTopRightRadius.toFloat()
            borderRadii[2] = borderRadii[3]
            borderRadii[5] = cornerBottomRightRadius.toFloat()
            borderRadii[4] = borderRadii[5]
            borderRadii[7] = cornerBottomLeftRadius.toFloat()
            borderRadii[6] = borderRadii[7]
            srcRadii[1] = cornerTopLeftRadius - borderWidth / 2.0f
            srcRadii[0] = srcRadii[1]
            srcRadii[3] = cornerTopRightRadius - borderWidth / 2.0f
            srcRadii[2] = srcRadii[3]
            srcRadii[5] = cornerBottomRightRadius - borderWidth / 2.0f
            srcRadii[4] = srcRadii[5]
            srcRadii[7] = cornerBottomLeftRadius - borderWidth / 2.0f
            srcRadii[6] = srcRadii[7]
        }
    }

    private fun calculateRadiiAndRectF(reset: Boolean) {
        if (reset) {
            cornerRadius = 0
        }
        calculateRadii()
        initBorderRectF()
        invalidate()
    }

    /**
     * 目前圆角矩形情况下不支持inner_border，需要将其置0
     */
    private fun clearInnerBorderWidth() {
        if (!isCircle) {
            innerBorderWidth = 0
        }
    }

    fun isCoverSrc(isCoverSrc: Boolean) {
        this.isCoverSrc = isCoverSrc
        initSrcRectF()
        invalidate()
    }

    fun isCircle(isCircle: Boolean) {
        this.isCircle = isCircle
        clearInnerBorderWidth()
        initSrcRectF()
        invalidate()
    }

    fun borderWidth(borderWidth: Float) {
        this.borderWidth = dp2px(borderWidth)
        calculateRadiiAndRectF(false)
    }

    fun borderColor(@ColorInt borderColor: Int) {
        this.borderColor = borderColor
        invalidate()
    }

    fun innerBorderWidth(innerBorderWidth: Float) {
        this.innerBorderWidth = dp2px(innerBorderWidth)
        clearInnerBorderWidth()
        invalidate()
    }

    fun innerBorderColor(@ColorInt innerBorderColor: Int) {
        this.innerBorderColor = innerBorderColor
        invalidate()
    }

    fun cornerRadius(cornerRadius: Float) {
        this.cornerRadius = dp2px(cornerRadius)
        calculateRadiiAndRectF(false)
    }

    fun cornerTopLeftRadius(cornerTopLeftRadius: Float) {
        this.cornerTopLeftRadius = dp2px(cornerTopLeftRadius)
        calculateRadiiAndRectF(true)
    }

    fun cornerTopRightRadius(cornerTopRightRadius: Float) {
        this.cornerTopRightRadius = dp2px(cornerTopRightRadius)
        calculateRadiiAndRectF(true)
    }

    fun cornerBottomLeftRadius(cornerBottomLeftRadius: Float) {
        this.cornerBottomLeftRadius = dp2px(cornerBottomLeftRadius)
        calculateRadiiAndRectF(true)
    }

    fun cornerBottomRightRadius(cornerBottomRightRadius: Float) {
        this.cornerBottomRightRadius = dp2px(cornerBottomRightRadius)
        calculateRadiiAndRectF(true)
    }

    fun maskColor(@ColorInt maskColor: Int) {
        this.maskColor = maskColor
        invalidate()
    }


    private fun dp2px(dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}