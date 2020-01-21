package com.aboust.develop_guide.kit.notify

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


const val CHANNEL_ID_DEFAULT = "application_notification"
const val CHANNEL_NAME_DEFAULT = "Application Notifications."
const val CHANNEL_DESCRIPTION_DEFAULT = "General application notifications."


/**
 * IMPORTANCE_NONE      关闭通知
 * IMPORTANCE_MIN       开启通知，不会弹出，但没有提示音，状态栏中无显示
 * IMPORTANCE_LOW       开启通知，不会弹出，不发出提示音，状态栏中显示
 * IMPORTANCE_DEFAULT   开启通知，不会弹出，发出提示音，状态栏中显示
 * IMPORTANCE_HIGH      开启通知，会弹出，发出提示音，状态栏中显示
 */

const val IMPORTANCE_MIN = NotificationCompat.PRIORITY_MIN
const val IMPORTANCE_LOW = NotificationCompat.PRIORITY_LOW
const val IMPORTANCE_NORMAL = NotificationCompat.PRIORITY_DEFAULT
const val IMPORTANCE_HIGH = NotificationCompat.PRIORITY_HIGH
const val IMPORTANCE_MAX = NotificationCompat.PRIORITY_MAX
const val IMPORTANCE_DEFAULT = NotificationManagerCompat.IMPORTANCE_DEFAULT
const val IMPORTANCE_UNSPECIFIED = NotificationManagerCompat.IMPORTANCE_UNSPECIFIED

const val NO_LIGHTS = 0
const val TYPE_NULL = -999