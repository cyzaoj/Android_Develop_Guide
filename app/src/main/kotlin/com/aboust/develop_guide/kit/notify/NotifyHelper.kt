package com.aboust.develop_guide.kit.notify

import android.app.NotificationManager
import android.os.Build
import android.text.Html
import androidx.core.app.NotificationCompat
import com.aboust.develop_guide.kit.notify.entities.NotifyChannel
import com.aboust.develop_guide.kit.notify.entities.Payload
import com.aboust.develop_guide.kit.notify.entities.RawNotification
import java.util.*


typealias Action = NotificationCompat.Action

fun getRandomInt(): Int = Random().nextInt(Int.MAX_VALUE - 100) + 100

object NotificationHelper {


    fun showNotification(
        notificationManager: NotificationManager,
        id: Int?,
        notification: NotificationCompat.Builder
    ): Int {
//        val key = NotifyExtender.getKey(notification.extras)
        val id = id ?: getRandomInt()

//        if (null != key) {
//            id = key.hashCode()
//            notificationManager.notify(key.toString(), id, notification.build())
//        } else {
        notificationManager.notify(id, notification.build())
//        }

        return id
    }


    fun createNotificationChannel(vararg channel: NotifyChannel) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val notificationManager = Notify.configuration.notificationManager
        channel.forEach {
            val ch = it.asNotificationChannel()
            if (null != ch) {
                notificationManager
                    ?.getNotificationChannel(it.id)
                    ?.run { return }
                notificationManager?.createNotificationChannel(ch)
            }
        }

    }

    /**
     * 构建
     */
    internal fun buildNotification(notify: Notify, payload: RawNotification)
            : NotificationCompat.Builder {

        val alerting = payload.alerting
        val channel = alerting.channel!!

        // 创建channel
        createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(notify.context, channel.id!!)
            .setColor(payload.header.color)
            .setSmallIcon(payload.header.icon)
            .setSubText(payload.header.name)
            .setShowWhen(payload.header.showTimestamp)
            .setAutoCancel(payload.metadata.autoCancel)
            .setContentIntent(payload.metadata.contentIntent)
            .setDeleteIntent(payload.metadata.deleteIntent)
            .setCategory(payload.metadata.category)
            .setLocalOnly(payload.metadata.localOnly)
            .setOngoing(payload.metadata.ongoing)
            .setTimeoutAfter(payload.metadata.timeout)

        //添加联系人
        payload.metadata.contacts.takeIf { it.isNotEmpty() }?.forEach {
            builder.addPerson(it)
        }

        // 创建标准内容
        if (payload.content is Payload.Content.Standard) builder
            .setContentTitle(payload.content.title)
            .setContentText(payload.content.text)

        // 创建大图标
        if (payload.content is Payload.Content.LargeIcon) builder.setLargeIcon(payload.content.largeIcon)

        payload.actions?.forEach {
            builder.addAction(it)
        }

        var style: NotificationCompat.Style? = null
        if (null === style) {
            style = setStyle(builder, payload.content)
        }
        builder.setStyle(style)
        return builder

    }


    private fun setStyle(
        builder: NotificationCompat.Builder,
        content: Payload.Content
    ): NotificationCompat.Style? {
        return when (content) {
            is Payload.Content.Default -> null
            is Payload.Content.TextList -> {
                NotificationCompat.InboxStyle().also { style ->
                    content.lines.forEach { style.addLine(it) }
                }
            }
            is Payload.Content.BigText -> {
                // Override the behavior of the second line.
                builder.setContentText(getAsSecondaryFormattedText((content.text ?: "").toString()))

                val bigText: CharSequence = Html.fromHtml(
                    "<font color='#3D3D3D'>" + (content.expandedText
                        ?: content.title) + "</font><br>" + content.bigText?.replace(
                        "\n".toRegex(),
                        "<br>"
                    )
                )

                NotificationCompat.BigTextStyle().bigText(bigText)
            }
            is Payload.Content.BigPicture -> {
                NotificationCompat.BigPictureStyle()
                    // This is the second line in the 'expanded' notification.
                    .setSummaryText(content.expandedText ?: content.text)
                    // This is the picture below.
                    .bigPicture(content.image)
                    .bigLargeIcon(null)

            }
            is Payload.Content.Message -> {
                NotificationCompat.MessagingStyle(content.userDisplayName)
                    .setConversationTitle(content.conversationTitle)
                    .also { s ->
                        content.messages.forEach { s.addMessage(it.text, it.timestamp, it.sender) }
                    }
            }
            is Payload.Content.Media -> null
        }
    }

    private fun getAsSecondaryFormattedText(str: String?): CharSequence? {
        str ?: return null
        return Html.fromHtml("<font color='#3D3D3D'>$str</font>")
    }

//    private fun setStyle(builder: NotificationCompat.Builder, content: Payload.Content): NotificationCompat.Style? {
//    }
}