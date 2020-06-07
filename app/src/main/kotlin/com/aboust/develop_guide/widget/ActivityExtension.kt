package com.aboust.develop_guide.widget

import android.content.Context
import androidx.core.app.ActivityOptionsCompat
import com.aboust.develop_guide.R


//跳转动画
fun translateLeftInRightOut(context: Context): ActivityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.translate_left_in, R.anim.translate_right_out)

fun translateRightInLeftOut(context: Context): ActivityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.translate_right_in, R.anim.translate_left_out)

fun translateBottomInBottomOut(context: Context): ActivityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.translate_bottom_in, R.anim.translate_bottom_out)
