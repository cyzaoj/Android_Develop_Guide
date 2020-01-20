package com.aboust.develop_guide.kit.notify.entities

import com.aboust.develop_guide.kit.notify.Action


internal data class RawNotification(
        internal val metadata: Payload.Metadata,
        internal val alerting: Payload.Alerts,
        internal val header: Payload.Header,
        internal val content: Payload.Content,
        internal val actions: ArrayList<Action>?,
        internal val group: Payload.Group?,
        internal val badge: Payload.Badge?


)
