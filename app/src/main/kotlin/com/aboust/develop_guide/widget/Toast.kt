package com.aboust.develop_guide.widget

import android.content.Context
import android.widget.Toast


fun String.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast? =
    Toast.makeText(context, this, duration).also { it.show() }




