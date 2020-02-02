package com.aboust.develop_guide.kit.notify.entities

import com.aboust.develop_guide.kit.notify.core.Action


internal data class RawNotification(
        internal val meta: Payload.Meta,
        internal val channel: NotifyChannel,
        internal val header: Payload.Header,
        internal val content: Payload.Content,
        internal val actions: MutableList<Action>?,
        internal val group: Payload.Group?,
        internal val badge: Payload.Badge?
)
