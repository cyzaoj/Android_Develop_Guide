package com.aboust.develop_guide.kit.notify

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.aboust.develop_guide.kit.notify.core.NotificationHelper
import com.aboust.develop_guide.kit.notify.core.NotifyCreator
import com.aboust.develop_guide.kit.notify.entities.RawNotification

class Notify private constructor(internal var context: Context) {

    companion object {

        internal var configuration = NotifyConfig()

        fun config(init: NotifyConfig.() -> Unit) = configuration.init()

        fun with(context: Context): NotifyCreator = NotifyCreator(Notify(context))
    }


    init {
        this.context = context.applicationContext
        if (null === configuration.notificationManager)
            configuration.notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        NotificationHelper.createNotificationChannel(*configuration.channels.toTypedArray())
    }

    internal fun asBuilder(payload: RawNotification): NotificationCompat.Builder {
        return NotificationHelper.buildNotification(this, payload)
    }

    internal fun show(id: Int?, builder: NotificationCompat.Builder): Int {
        return NotificationHelper.showNotification(configuration.notificationManager!!, id, builder)
    }

}