package com.aboust.develop_guide.activity

import android.annotation.SuppressLint
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
import com.aboust.develop_guide.kit.notify.Notify
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.common_toolbar.*


/**
 * 通知
 */
class NotificationActivity : AppCompatActivity() {

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        toolbar_left.setOnClickListener { this.finish() }
        toolbar_title.text = getString(R.string.notification)

        notification_default.setOnClickListener {
            Notify
                    .with(this)
                    .content {
                        title = "Default Notification"
                        text = "Default Notification !!!"
                    }
                    .show()
        }

        notification_big_text.setOnClickListener {
            Notify
                    .with(this)
                    .asBigText {
                        title = "This is a BigText"
                        text = "this is a bigText!!!"
                        expandedText = "Mouthwatering deliciousness."
                        bigText =
                                "this is a bigText!\nthis is a bigText!\nthis is a bigText!\nthis is a bigText!\nthis is a bigText!\n"
                    }
                    .show()
        }

        notification_big_picture.setOnClickListener {
            Notify
                    .with(this)
                    .asBigPicture {
                        picture = getBitmap(this@NotificationActivity, R.mipmap.ic_launcher)
                        title = "This is a BigPicture"
                        text = "this is a BigPicture!!!"
                        expandedText = "Mouthwatering deliciousness."
                    }.append {
                    }
                    .show()
        }
    }


    private fun getBitmap(context: Context, @DrawableRes vectorDrawableId: Int): Bitmap? {
        var bitmap: Bitmap? = null
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val vectorDrawable: Drawable? = context.getDrawable(vectorDrawableId)
            bitmap = Bitmap.createBitmap(
                    vectorDrawable!!.intrinsicWidth,
                    vectorDrawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
        } else {
            bitmap = BitmapFactory.decodeResource(context.resources, vectorDrawableId)
        }
        return bitmap
    }

}