package com.aboust.develop_guide.kit.notify.config

import android.app.NotificationManager
import com.aboust.develop_guide.kit.notify.entities.Payload
import com.aboust.develop_guide.kit.notify.entities.NotifyChannel

data class NotifyConfig(
    internal var notificationManager: NotificationManager? = null,
    internal var header: Payload.Header = Payload.Header(),
    internal val channels: MutableList<NotifyChannel> = ArrayList()

) {


    fun alerting() = Payload.Alerts(
        NotifyChannel
            (CHANNEL_ID_DEFAULT, CHANNEL_NAME_DEFAULT, IMPORTANCE_DEFAULT)
            .description(CHANNEL_DESCRIPTION_DEFAULT)
    )


    /**
     * 添加渠道
     */
    fun channels(init: List<NotifyChannel>.() -> Unit) {
        channels.init()
    }

    /**
     * 添加通知栏头信息
     */
    fun header(init: Payload.Header.() -> Unit): NotifyConfig {
        header.init()
        return this
    }


}