package com.aboust.develop_guide.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt


class DownloadNotifier(val context: Context) {

    private var notificationManager: NotificationManager? = null
    private var notifyBuilder: NotificationCompat.Builder? = null
    private val localID = ThreadLocal<Int>()


    companion object {

        private const val CHANNEL_ID = "download:notifier"
        private const val CHANNEL_NAME = "AppDownload"

//        fun getBitmap(context: Context, @DrawableRes vectorDrawableId: Int): Bitmap? = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            val vectorDrawable: Drawable? = context.getDrawable(vectorDrawableId)
//            val bitmap = Bitmap.createBitmap(
//                    vectorDrawable!!.intrinsicWidth,
//                    vectorDrawable.intrinsicHeight,
//                    Bitmap.Config.ARGB_8888
//            )
//            val canvas = Canvas(bitmap)
//            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
//            vectorDrawable.draw(canvas)
//            bitmap
//        } else {
//            BitmapFactory.decodeResource(context.resources, vectorDrawableId)
//        }
    }

    init {
        notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            //设置绕过免打扰模式
//            channel.setBypassDnd(false);
//            //检测是否绕过免打扰模式
//            channel.canBypassDnd();
//            //设置在锁屏界面上显示这条通知
//            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
//            channel.setLightColor(Color.GREEN);
//            channel.setShowBadge(true);
//            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.enableVibration(false)
            channel.enableLights(false)
            notificationManager?.createNotificationChannel(channel)
        }

        notifyBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
    }

    fun create(id: Int, icon: Int, title: CharSequence?, description: CharSequence?) {
        localID.set(id)
        setUpNotification(id, icon, title, description)
    }

    fun stop(icon: Int, title: CharSequence?) {
        val id = localID.get()
        id?.let { stop(it, icon, title) }
    }

    fun update(total: Long, offset: Long) {
        val id = localID.get()
        id?.let { update(it, total, offset) }
    }


    /**
     * 创建通知
     */
    private fun setUpNotification(id: Int, icon: Int, title: CharSequence?, description: CharSequence?) {
        notifyBuilder?.let {
            it.setContentTitle(title)
                    .setContentText(description)
                    .setSmallIcon(icon)
                    .setLargeIcon(getAppIcon(this.context))
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .priority = NotificationCompat.PRIORITY_LOW
            notificationManager?.notify(id, it.build())
        }
    }

    private fun stop(id: Int, icon: Int, title: CharSequence?) {
        notifyBuilder?.let {
            it
                    .setContentTitle(title)
                    .setSmallIcon(icon)
                    .setLargeIcon(getAppIcon(this.context))
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
            val notification: Notification = it.build()
            notification.flags = Notification.FLAG_AUTO_CANCEL or Notification.FLAG_ONLY_ALERT_ONCE
            notificationManager?.notify(id, notification)
        }
    }

    /**
     * 更新通知
     */
    private var prevRate = 0

    private fun update(id: Int, total: Long, offset: Long) {
        val progress = (offset * 1.0F) / total
        //做一下判断，防止自回调过于频繁，造成更新通知栏进度过于频繁，而出现卡顿的问题。
        val rate = (progress * 100).roundToInt()
        if (prevRate == rate) return
        notifyBuilder?.setContentText("${rate}%")
        notifyBuilder?.setProgress(100, rate, false)
        notificationManager?.notify(id, notifyBuilder?.build())
        this.prevRate = rate
    }


//    private fun appIcon(): Bitmap? {
//        val packageManager = this.context.applicationContext.packageManager
//        val appInfo = packageManager.getApplicationInfo(context.packageName, 0)
//        val d: Drawable = packageManager.getApplicationIcon(appInfo)
//        val bd = d as BitmapDrawable
//        return bd.bitmap
//    }

    private fun getAppIcon(context: Context?): Bitmap? {
        val ctx = context?.applicationContext
        val packageManager = ctx?.packageManager
        val packageName = ctx?.packageName
        if (TextUtils.isEmpty(packageName)) return null
        val drawable = packageManager?.getApplicationIcon(packageName!!)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            when (drawable) {
                is BitmapDrawable -> drawable.bitmap
                is AdaptiveIconDrawable -> {
                    val bitmap =
                            Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                    bitmap
                }
                else -> null
            }
        } else {
            (drawable as BitmapDrawable).bitmap
        }
    }

}