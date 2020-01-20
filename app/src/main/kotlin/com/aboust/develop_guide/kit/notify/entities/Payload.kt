package com.aboust.develop_guide.kit.notify.entities

import android.app.PendingIntent
import android.graphics.Bitmap
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.Person


sealed class Payload {

    /**
     * groupKey: 摘要通知添加到通知组中
     * groupSummary: 指定将其用作通知组摘要
     */
    data class Group(var key: String? = null, var summary: Boolean? = null)

    data class Badge(var showBadge: Boolean = false,
                     var badgeNumber: Int = -1,
                     var badgeIconType: Int = NotificationCompat.BADGE_ICON_SMALL,
                     @DrawableRes var icon: Int = -1)

    data class Metadata(

            var contentIntent: PendingIntent? = null,

            var deleteIntent: PendingIntent? = null,

            var autoCancel: Boolean = true,

            var category: String? = null,

            var localOnly: Boolean = false,

            var ongoing: Boolean = false,

            var timeout: Long = 0L,


            internal val contacts: ArrayList<String> = ArrayList()
    ) {
        fun people(init: ArrayList<String>.() -> Unit) = contacts.init()
    }


    /**
     * 通知详解
     *  https://developer.android.google.cn/guide/topics/ui/notifiers/notifications.html?hl=zh-cn#Templates
     */
    data class Header(
            /**
             *  ① **小图标：** 此为必要图标，通过 setSmallIcon() 设置。
             */
            @DrawableRes var icon: Int = -1,

            /**
             * The color of the notification items -- icon, appName, and expand indicator.
             */
            @ColorInt var color: Int = 0x4A90E2,

            /**
             * ② **应用名称：** 此由系统提供。
             */
            var name: CharSequence? = null,

            /**
             *  ③ **时间戳：** 此由系统提供，不过您可以通过 setWhen() 进行替换，或使用 setShowWhen(false) 将其隐藏。
             */
            var showWhen: Boolean = true
    )

    sealed class Content {


        /**
         * 标准 通知只支持
         * 标题和第二行文字
         *
         */
        interface Standard {
            var title: CharSequence?
            var text: CharSequence?
        }

        /**
         * 大图标模式
         */
        interface LargeIcon {
            var largeIcon: Bitmap?
        }

        /**
         *
         */
        interface Expandable {
            var expandedText: CharSequence?
        }

        /**
         * 默认模式
         * title
         * text
         * largeIcon
         *
         */
        data class Default(
                override var title: CharSequence? = null,
                override var text: CharSequence? = null,
                override var largeIcon: Bitmap? = null
        ) : Content(), Standard, LargeIcon

        data class BigText(
                override var title: CharSequence? = null,
                override var text: CharSequence? = null,
                override var largeIcon: Bitmap? = null,
                override var expandedText: CharSequence? = null,
                var bigText: CharSequence? = null
        ) : Content(), Standard, LargeIcon, Expandable

        data class TextList(
                override var title: CharSequence? = null,
                override var text: CharSequence? = null,
                override var largeIcon: Bitmap? = null,
                var lines: List<CharSequence> = ArrayList()
        ) : Content(), Standard, LargeIcon

        data class BigPicture(
                override var title: CharSequence? = null,
                override var text: CharSequence? = null,
                override var largeIcon: Bitmap? = null,
                override var expandedText: CharSequence? = null,
                var picture: Bitmap? = null
        ) : Content(), Standard, LargeIcon, Expandable

        data class Message(
                override var largeIcon: Bitmap? = null,

                /**
                 * 显示在会话上方的标题
                 */
                var conversationTitle: CharSequence? = null,

                /**
                 * 发消息的用户名称
                 */
//            var userDisplayName: CharSequence = "",
                var user: Person = Person.Builder().build(),

                /**
                 * 消息内容
                 */
                var messages: List<NotificationCompat.MessagingStyle.Message> = ArrayList()
        ) : Content(), LargeIcon

        data class Media(
                override var title: CharSequence?,
                override var text: CharSequence?,
                override var largeIcon: Bitmap?
        ) : Content(), Standard, LargeIcon
    }


    data class Alerts(
            val channel: NotifyChannel
//        @NotificationCompat.NotificationVisibility var lockScreenVisibility: Int = NotificationCompat.VISIBILITY_PRIVATE,
//
//        val channelId: String = CHANNEL_ID_DEFAULT,
//
//        var channelName: String = CHANNEL_NAME_DEFAULT,
//
//        var channelDescription: String = CHANNEL_DESCRIPTION_DEFAULT,
//
//        @Importance var channelImportance: Int = IMPORTANCE_NORMAL,
//
//        @ColorInt var lightColor: Int = NO_LIGHTS,
//
//        var vibrationPattern: List<Long> = ArrayList(),
//
//        var sound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    )
}