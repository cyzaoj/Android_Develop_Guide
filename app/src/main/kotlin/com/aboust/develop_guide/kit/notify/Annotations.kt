package com.aboust.develop_guide.kit.notify

import androidx.annotation.IntDef
import androidx.core.app.NotificationManagerCompat

/**
 * 试验性的
 * 某些方法不具备生产环境的支持，只能使用特定手段使用的注解
 */
annotation class Experimental


@Retention(AnnotationRetention.SOURCE)
@IntDef(
        NotificationManagerCompat.IMPORTANCE_UNSPECIFIED,
        NotificationManagerCompat.IMPORTANCE_NONE,
        NotificationManagerCompat.IMPORTANCE_MIN,
        NotificationManagerCompat.IMPORTANCE_LOW,
        NotificationManagerCompat.IMPORTANCE_DEFAULT,
        NotificationManagerCompat.IMPORTANCE_HIGH
)
annotation class Importance





