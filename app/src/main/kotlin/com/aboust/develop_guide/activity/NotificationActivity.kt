package com.aboust.develop_guide.activity

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.aboust.develop_guide.R
import com.aboust.develop_guide.kit.notify.Notify
import com.aboust.develop_guide.kit.notify.entities.NotifyChannel
import com.aboust.develop_guide.kit.notify.getRandomInt
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.util.*

/**
 * 通知
 */
class NotificationActivity : AppCompatActivity() {

    private var notificationManager: NotificationManager? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        toolbar_left.setOnClickListener { this.finish() }
        toolbar_title.text = getString(R.string.notification)

        notification_default.setOnClickListener {
            Notify
                .with(this)
                .asBigPicture {
                    title = "Chocolate brownie sundae"
                    text = "Get a look at this amazing dessert!"
                    expandedText = "The delicious brownie sundae now available."
                    image = BitmapFactory.decodeResource(
                        this@NotificationActivity.resources,
                        R.mipmap.ic_launcher
                    )
                }
                .show()
        }

        notification_big_text.setOnClickListener {
            Notify
                .with(this)
                .asBigText {
                    title = "Chocolate brownie sundae"
                    text = "Try our newest dessert option!"
                    expandedText = "Mouthwatering deliciousness."
                    bigText =
                        "Our own Fabulous Godiva Chocolate Brownie, Vanilla Ice Cream, Hot Fudge, Whipped Cream and Toasted Almonds.\n" +
                                "\n" +
                                "Come try this delicious new dessert and get two for the price of one!"
                }
                .show()
        }
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)


    }


    companion object {
        const val CHANNEL_ID = "1"
    }

}