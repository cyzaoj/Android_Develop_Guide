package com.aboust.develop_guide.widget

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.aboust.develop_guide.R


class InputCodeView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs), TextWatcher, View.OnKeyListener, OnFocusChangeListener {
    var onCodeFinishListener: OnCodeFinishListener? = null

    /**
     * 输入框数量
     */
    private var length: Int

    /**
     * 输入框类型
     */
    private var mEtInputType: VCInputType

    /**
     * 输入框的宽度
     */
    private var textEditWidth: Int

    /**
     * 文字颜色
     */
    private var textColor: Int

    /**
     * 文字大小
     */
    private var textSize: Float

    /**
     * 输入框背景
     */
    private var textEditBackground: Int

    /**
     * 输入框间距
     */
    private var mEtSpacing = 0

    /**
     * 平分后的间距
     */
    private var mEtBisectSpacing = 0

    /**
     * 判断是否平分
     */
    private val isBisect: Boolean

    /**
     * 是否显示光标
     */
    private val cursorVisible: Boolean

    /**
     * 光标样式
     */
    private var mCursorDrawable: Int

    /**
     * 输入框宽度
     */
    private var mViewWidth = 0

    /**
     * 输入框间距
     */
    private val mViewMargin = 0

//    fun getmEtNumber(): Int {
//        return length
//    }
//
//    fun setmEtNumber(mEtNumber: Int) {
//        this.length = mEtNumber
//    }
//
//    fun getmEtInputType(): VCInputType {
//        return mEtInputType
//    }
//
//    fun setmEtInputType(mEtInputType: VCInputType) {
//        this.mEtInputType = mEtInputType
//    }
//
//    fun getmEtWidth(): Int {
//        return textEditWidth
//    }
//
//    fun setmEtWidth(mEtWidth: Int) {
//        this.textEditWidth = mEtWidth
//    }
//
//    fun getmEtTextColor(): Int {
//        return textColor
//    }
//
//    fun setmEtTextColor(mEtTextColor: Int) {
//        this.textColor = mEtTextColor
//    }
//
//    fun getmEtTextSize(): Float {
//        return textSize
//    }
//
//    fun setmEtTextSize(mEtTextSize: Float) {
//        this.textSize = mEtTextSize
//    }
//
//    fun getmEtTextBg(): Int {
//        return textEditBackground
//    }
//
//    fun setmEtTextBg(mEtTextBg: Int) {
//        this.textEditBackground = mEtTextBg
//    }
//
//    fun getmCursorDrawable(): Int {
//        return mCursorDrawable
//    }
//
//    fun setmCursorDrawable(mCursorDrawable: Int) {
//        this.mCursorDrawable = mCursorDrawable
//    }

    enum class VCInputType {
        /**
         * 数字类型
         */
        NUMBER,

        /**
         * 数字密码
         */
        NUMBERPASSWORD,

        /**
         * 文字
         */
        TEXT,

        /**
         * 文字密码
         */
        TEXTPASSWORD
    }

    private fun initView() {
        for (i in 0 until length) {
            val editText = EditText(context)
            initEditText(editText, i)
            addView(editText)
            //设置第一个editText获取焦点
            if (0 == i) editText.isFocusable = true

        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun initEditText(editText: EditText, i: Int) {
        editText.layoutParams = getLayoutParams(i)
        editText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        editText.gravity = Gravity.CENTER
        editText.id = i
        editText.isCursorVisible = false
        editText.maxEms = 1
        editText.setTextColor(textColor)
        editText.textSize = textSize
        editText.isCursorVisible = cursorVisible
        editText.maxLines = 1
        editText.filters = arrayOf<InputFilter>(LengthFilter(1))
        when (mEtInputType) {
            VCInputType.NUMBER -> editText.inputType = InputType.TYPE_CLASS_NUMBER
            VCInputType.NUMBERPASSWORD -> {
                editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                editText.transformationMethod = AsteriskPasswordTransformationMethod()
            }
            VCInputType.TEXT -> editText.inputType = InputType.TYPE_CLASS_TEXT
            VCInputType.TEXTPASSWORD -> editText.inputType = InputType.TYPE_CLASS_NUMBER
            else -> editText.inputType = InputType.TYPE_CLASS_NUMBER
        }
        editText.setPadding(0, 0, 0, 0)
        editText.setOnKeyListener(this)
        editText.setBackgroundResource(textEditBackground)
        editTextCursorDrawable(editText)
        editText.addTextChangedListener(this)
        editText.setOnKeyListener(this)
        editText.onFocusChangeListener = this
    }

    /**
     * 获取EditText 的 LayoutParams
     */
    fun getLayoutParams(i: Int): LayoutParams {
        val layoutParams = LayoutParams(textEditWidth, textEditWidth)
        if (!isBisect) {
            //平分Margin，把第一个EditText跟最后一个EditText的间距同设为平分
            mEtBisectSpacing = (mViewWidth - length * textEditWidth) / (length + 1)
            if (i == 0) {
                layoutParams.leftMargin = mEtBisectSpacing
                layoutParams.rightMargin = mEtBisectSpacing / 2
            } else if (i == length - 1) {
                layoutParams.leftMargin = mEtBisectSpacing / 2
                layoutParams.rightMargin = mEtBisectSpacing
            } else {
                layoutParams.leftMargin = mEtBisectSpacing / 2
                layoutParams.rightMargin = mEtBisectSpacing / 2
            }
        } else {
            layoutParams.leftMargin = mEtSpacing / 2
            layoutParams.rightMargin = mEtSpacing / 2
        }
        layoutParams.gravity = Gravity.CENTER
        return layoutParams
    }

    fun editTextCursorDrawable(editText: EditText?) {
        //修改光标的颜色（反射）
        if (cursorVisible) {
            try {
                val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
                f.isAccessible = true
                f[editText] = mCursorDrawable
            } catch (ignored: Exception) {
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewWidth = measuredWidth
        updateETMargin()
    }

    private fun updateETMargin() {
        for (i in 0 until length) {
            val editText = getChildAt(i) as EditText
            editText.layoutParams = getLayoutParams(i)
        }
    }

    override fun onFocusChange(view: View, b: Boolean) {
        if (b) focus()
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        if (s.isNotEmpty()) focus()

        onCodeFinishListener?.onTextChange(this, result)
        //如果最后一个输入框有字符，则返回结果
        val lastEditText = getChildAt(length - 1) as EditText
        if (lastEditText.text.isNotEmpty()) onCodeFinishListener?.onComplete(this, result)

    }

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
            backFocus()
        }
        return false
    }

    override fun setEnabled(enabled: Boolean) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.isEnabled = enabled
        }
    }

    /**
     * 获取焦点
     */
    private fun focus() {
        val count = childCount
        var editText: EditText
        //利用for循环找出还最前面那个还没被输入字符的EditText，并把焦点移交给它。
        for (i in 0 until count) {
            editText = getChildAt(i) as EditText
            if (editText.text.isEmpty()) {
                editText.isCursorVisible = cursorVisible
                editText.requestFocus()
                return
            } else {
                editText.isCursorVisible = false
                if (i == count - 1) {
                    editText.requestFocus()
                }
            }
        }
    }

    private fun backFocus() {
        var editText: EditText
        //循环检测有字符的`editText`，把其置空，并获取焦点。
        for (i in length - 1 downTo 0) {
            editText = getChildAt(i) as EditText
            if (editText.text.isNotEmpty()) {
                editText.setText("")
                editText.isCursorVisible = cursorVisible
                editText.requestFocus()
                return
            }
        }
    }

    private val result: String
        private get() {
            val stringBuffer = StringBuilder()
            var editText: EditText
            for (i in 0 until length) {
                editText = getChildAt(i) as EditText
                stringBuffer.append(editText.text)
            }
            return stringBuffer.toString()
        }

    interface OnCodeFinishListener {
        /**
         * 文本改变
         */
        fun onTextChange(view: View?, content: String?)

        /**
         * 输入完成
         */
        fun onComplete(view: View?, content: String?)
    }

    /**
     * 清空验证码输入框
     */
    fun clear() {
        var editText: EditText
        for (i in length - 1 downTo 0) {
            editText = getChildAt(i) as EditText
            editText.setText("")
            if (i == 0) {
                editText.isCursorVisible = cursorVisible
                editText.requestFocus()
            }
        }
    }

    init {
        @SuppressLint("CustomViewStyleable")
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.vericationCodeView)
        length = typedArray.getInteger(R.styleable.vericationCodeView_vcv_et_number, 4)
        val inputType = typedArray.getInt(R.styleable.vericationCodeView_vcv_et_inputType, VCInputType.NUMBER.ordinal)
        mEtInputType = VCInputType.values()[inputType]
        textEditWidth = typedArray.getDimensionPixelSize(R.styleable.vericationCodeView_vcv_et_width, 120)
        textColor = typedArray.getColor(R.styleable.vericationCodeView_vcv_et_text_color, Color.BLACK)
        textSize = typedArray.getDimensionPixelSize(R.styleable.vericationCodeView_vcv_et_text_size, 16).toFloat()
        textEditBackground = typedArray.getResourceId(R.styleable.vericationCodeView_vcv_et_bg, R.drawable.et_login_code)
        mCursorDrawable = typedArray.getResourceId(R.styleable.vericationCodeView_vcv_et_cursor, R.drawable.et_cursor)
        cursorVisible = typedArray.getBoolean(R.styleable.vericationCodeView_vcv_et_cursor_visible, true)
        isBisect = typedArray.hasValue(R.styleable.vericationCodeView_vcv_et_spacing)
        if (isBisect) mEtSpacing = typedArray.getDimensionPixelSize(R.styleable.vericationCodeView_vcv_et_spacing, 0)
        initView()
        //释放资源
        typedArray.recycle()
    }
}

class AsteriskPasswordTransformationMethod : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }

    class PasswordCharSequence(private val mSource: CharSequence) : CharSequence {
        override val length: Int
            get() = mSource.length

        override fun get(index: Int): Char = '•'

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = mSource.subSequence(startIndex, endIndex)

    }

}

