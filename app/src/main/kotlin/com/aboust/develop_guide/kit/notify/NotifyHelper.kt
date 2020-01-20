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
        val channel = alerting.channel

        // 创建channel
        createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(notify.context, channel.id!!)
                .setColor(payload.header.color)
                .setSmallIcon(payload.header.icon)
                .setSubText(payload.header.name)
                .setShowWhen(payload.header.showWhen)
                .setAutoCancel(payload.metadata.autoCancel)
                .setContentIntent(payload.metadata.contentIntent)
                .setDeleteIntent(payload.metadata.deleteIntent)
                .setCategory(payload.metadata.category)
                .setLocalOnly(payload.metadata.localOnly)
                .setOngoing(payload.metadata.ongoing)
                .setTimeoutAfter(payload.metadata.timeout)

        payload.badge.takeIf { it !== null }?.let {
            if (!it.showBadge) return@let

            val badgeIconType = it.badgeIconType
            val icon = it.icon
            val badgeNumber = it.badgeNumber
            if (badgeNumber >= 0) {
                builder.setNumber(badgeNumber)
            }
            if (badgeIconType >= 0) {
                builder.setBadgeIconType(it.badgeIconType)
            }
            if (icon >= 0) {
                builder.setSmallIcon(it.icon)
            }
        }
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


    private fun setStyle(builder: NotificationCompat.Builder, content: Payload.Content)
            : NotificationCompat.Style? {
        return when (content) {
            is Payload.Content.Default -> null
            is Payload.Content.TextList -> {
                NotificationCompat.InboxStyle().also { style ->
                    content.lines.forEach { style.addLine(it) }
                }
            }
            is Payload.Content.BigText -> {
                builder.setContentText(getAsSecondaryFormattedText((content.text ?: "").toString()))
                val ht = "<font color='#3D3D3D'>${content.expandedText
                        ?: content.title}</font><br>${content.bigText?.replace("\n".toRegex(), "<br>")}"
                val bigText: CharSequence = renderHtml(ht)
                NotificationCompat.BigTextStyle().bigText(bigText)
            }
            is Payload.Content.BigPicture -> {
                NotificationCompat.BigPictureStyle()
                        // This is the second line in the 'expanded' notification.
                        .setSummaryText(content.expandedText ?: content.text)
                        .bigPicture(content.picture)

                        //要使该图片仅在通知收起时显示为缩略图，
                        // 请调用 setLargeIcon() 并传入该图片，
                        // 同时调用 BigPictureStyle.bigLargeIcon() 并传入 null，这样大图标就会在通知展开时消失：
                        .bigLargeIcon(null)
            }
            is Payload.Content.Message -> {
                NotificationCompat.MessagingStyle(content.user)
                        .setConversationTitle(content.conversationTitle)
                        .also { s ->
                            content.messages.forEach {
                                s.addMessage(
                                        it.text,
                                        it.timestamp,
                                        s.user
                                )
                            }
                        }
            }
            is Payload.Content.Media -> null
        }
    }

    private fun getAsSecondaryFormattedText(str: String?): CharSequence? {
        str ?: return null
        val ht = "<font color='#3D3D3D'>$str</font>"
        return renderHtml(ht)
    }

    private fun renderHtml(str: String?): CharSequence {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  FROM_HTML_MODE_COMPACT：html块元素之间使用一个换行符分隔
            //  FROM_HTML_MODE_LEGACY：html块元素之间使用两个换行符分隔
            Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(str)
        }
    }
//    private fun setStyle(builder: NotificationCompat.Builder, content: Payload.Content): NotificationCompat.Style? {
//    }
}