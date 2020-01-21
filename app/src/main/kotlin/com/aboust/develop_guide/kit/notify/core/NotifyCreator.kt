package com.aboust.develop_guide.kit.notify.core

import androidx.core.app.NotificationCompat
import com.aboust.develop_guide.kit.notify.Notify
import com.aboust.develop_guide.kit.notify.entities.NotifyChannel
import com.aboust.develop_guide.kit.notify.entities.Payload
import com.aboust.develop_guide.kit.notify.entities.RawNotification


class NotifyCreator internal constructor(private val notify: Notify) {

    private var metadata = Payload.Meta()
    private var alerts: Payload.Alerts = Notify.configuration.alerting()
    private var header = Notify.configuration.header.copy()
    private var content: Payload.Content = Payload.Content.Default()

    private var actions: MutableList<Action>? = null
    private var badge: Payload.Badge? = null
    private var group: Payload.Group? = null

//    private var extend: ((NotificationCompat.Builder) -> Unit?)? = null

    fun metadata(init: Payload.Meta.() -> Unit): NotifyCreator {
        this.metadata.init()
        return this
    }

//    fun extend(extend: (NotificationCompat.Builder) -> Unit?): NotifyCreator {
//        this.extend = extend
//        return this
//    }

    fun header(init: Payload.Header.() -> Unit): NotifyCreator {
        this.header.init()
        return this
    }

    fun alerting(channel: NotifyChannel, init: Payload.Alerts.() -> Unit): NotifyCreator {
        this.alerts = this.alerts.copy(channel = channel).also(init)
        return this
    }

    fun group(init: Payload.Group.() -> Unit): NotifyCreator {
        this.group = Payload.Group().also(init)
        return this
    }

    fun badge(init: Payload.Badge.() -> Unit): NotifyCreator {
        this.badge = Payload.Badge().also(init)
        return this
    }


    fun content(init: Payload.Content.Default.() -> Unit): NotifyCreator {
        this.content = Payload.Content.Default().also(init)
        return this
    }

    fun asTextList(init: Payload.Content.TextList.() -> Unit): NotifyCreator {
        this.content = Payload.Content.TextList().also(init)
        return this
    }

    fun asBigText(init: Payload.Content.BigText.() -> Unit): NotifyCreator {
        this.content = Payload.Content.BigText().also(init)
        return this
    }

    fun asBigPicture(init: Payload.Content.BigPicture.() -> Unit): NotifyCreator {
        this.content = Payload.Content.BigPicture().also(init)
        return this
    }

    fun actions(init: ArrayList<Action>.() -> Unit): NotifyCreator {
        this.actions = ArrayList<Action>().also(init)
        return this
    }


    fun asBuilder(): NotificationCompat.Builder {
        //        extend?.let { exec -> exec(builder) }
        return notify.asBuilder(RawNotification(metadata, alerts, header, content, actions, group, badge))
    }


    fun show(id: Int? = null): Int = notify.show(id, asBuilder())

    fun cancel(id: Int) = NotifyCompact.cancelNotification(Notify.configuration.notificationManager!!, id)
}