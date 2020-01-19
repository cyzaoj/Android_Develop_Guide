package com.aboust.develop_guide.kit.notify.config

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


const val CHANNEL_ID_DEFAULT = "application_notification"
const val CHANNEL_NAME_DEFAULT = "Application Notifications."
const val CHANNEL_DESCRIPTION_DEFAULT = "General application notifications."


const val IMPORTANCE_MIN = NotificationCompat.PRIORITY_MIN
const val IMPORTANCE_LOW = NotificationCompat.PRIORITY_LOW
const val IMPORTANCE_NORMAL = NotificationCompat.PRIORITY_DEFAULT
const val IMPORTANCE_HIGH = NotificationCompat.PRIORITY_HIGH
const val IMPORTANCE_MAX = NotificationCompat.PRIORITY_MAX
const val IMPORTANCE_DEFAULT = NotificationManagerCompat.IMPORTANCE_DEFAULT
const val IMPORTANCE_UNSPECIFIED = NotificationManagerCompat.IMPORTANCE_UNSPECIFIED

const val NO_LIGHTS = 0
