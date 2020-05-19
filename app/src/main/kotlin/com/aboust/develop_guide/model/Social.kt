package com.aboust.develop_guide.model

import android.content.Context
import android.content.pm.PackageManager
import com.aboust.develop_guide.model.Social.Companion.WE_CHAT_APP_ID
import com.aboust.develop_guide.model.Social.Companion.WE_CHAT_SECRET

/**
 * 第三方登录信息
 */
sealed class Social {

    data class Wechat(val appId: String, val secret: String)

    companion object {
        const val WE_CHAT_APP_ID = "WEIXIN_APP_ID"
        const val WE_CHAT_SECRET = "WEIXIN_APP_SECRET"
    }
}


object SocialConfig {
    var weixin: Social.Wechat? = null


    fun create(context: Context) {
        val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val weId = appInfo.metaData.getString(WE_CHAT_APP_ID, "")
        val weSecretKey = appInfo.metaData.getString(WE_CHAT_SECRET, "")
        weixin = Social.Wechat(weId, weSecretKey)
    }
}