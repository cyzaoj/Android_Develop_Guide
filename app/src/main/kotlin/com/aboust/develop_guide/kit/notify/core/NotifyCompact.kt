package com.aboust.develop_guide.kit.notify.core

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat

/**
 * 通知工具类
 * 1. 渠道构建管理
 * 2. 通知构建管理
 */
internal object NotifyCompact {

    /**
     * 取消通知
     */
    fun cancelNotification(notificationManager: NotificationManager, notificationId: Int) = notificationManager.cancel(notificationId)

    /**
     * 取消所有通知
     */
    fun cancelAllNotification(context: Context) = NotificationManagerCompat.from(context).cancelAll()

    /**
     * 根据tag
     */
    fun cancelNotification(context: Context, tag: String?, id: Int) = NotificationManagerCompat.from(context).cancel(tag, id)



}