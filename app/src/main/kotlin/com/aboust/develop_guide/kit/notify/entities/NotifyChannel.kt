package com.aboust.develop_guide.kit.notify.entities

import android.app.NotificationChannel
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import com.aboust.develop_guide.kit.notify.Importance
import com.aboust.develop_guide.kit.notify.NO_LIGHTS

/**
 * 通知通道
 *
 * https://developer.android.google.cn/training/notify-user/channels?hl=zh-cn
 *
 */
class NotifyChannel(val id: String?, val name: CharSequence?, @Importance val importance: Int) {

    private var notificationChannel: NotificationChannel? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(id, name, importance)
        }
    }

    fun bypassDnd(bypassDnd: Boolean): NotifyChannel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel?.setBypassDnd(bypassDnd)
        }
        return this
    }


    fun description(description: String): NotifyChannel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel?.description = description
        }
        return this
    }


    fun group(groupId: String): NotifyChannel? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel?.group = groupId
        }
        return this
    }

    fun importance(@Importance importance: Int): NotifyChannel? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel?.importance = importance
        }
        return this
    }


    fun lightColor(argb: Int): NotifyChannel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (NO_LIGHTS != argb) {
                notificationChannel?.lightColor = argb
                notificationChannel?.enableLights(true)
            }
        }
        return this
    }


    fun lockScreenVisibility(lockScreenVisibility: Int): NotifyChannel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel?.lockscreenVisibility = lockScreenVisibility
        }
        return this
    }


    fun name(name: CharSequence): NotifyChannel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel?.name = name
        }
        return this
    }


    fun showBadge(showBadge: Boolean): NotifyChannel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel?.setShowBadge(showBadge)
        }
        return this
    }


    fun sound(sound: Uri?, audioAttributes: AudioAttributes = AudioAttributes.Builder().build()
    ): NotifyChannel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel?.setSound(sound, audioAttributes)
        }
        return this
    }

    fun vibrationPattern(vibrationPattern: LongArray): NotifyChannel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel?.vibrationPattern = vibrationPattern
            notificationChannel?.enableVibration(true)
        }
        return this
    }

    fun asNotificationChannel() = this.notificationChannel

}