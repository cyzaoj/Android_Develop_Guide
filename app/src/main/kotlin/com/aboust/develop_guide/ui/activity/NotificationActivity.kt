package com.aboust.develop_guide.ui.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.aboust.develop_guide.R
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.common_toolbar.*


/**
 * 通知
 */
class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        toolbar_left.setOnClickListener { this.finish() }
        toolbar_title.text = getString(R.string.notification)

        notification_default.setOnClickListener {

        }

        notification_big_text.setOnClickListener {

        }

        notification_big_picture.setOnClickListener {

        }
    }

    private fun getBitmap(context: Context, @DrawableRes vectorDrawableId: Int): Bitmap? = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        val vectorDrawable: Drawable? = context.getDrawable(vectorDrawableId)
        val bitmap = Bitmap.createBitmap(
                vectorDrawable!!.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        bitmap
    } else {
        BitmapFactory.decodeResource(context.resources, vectorDrawableId)
    }


}