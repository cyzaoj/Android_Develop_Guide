package com.aboust.develop_guide.wxapi

import android.app.Activity
import android.os.Bundle
import com.aboust.develop_guide.model.SocialConfig
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import timber.log.Timber
import java.lang.IllegalArgumentException


class WXEntryActivity : Activity(), IWXAPIEventHandler {

    private lateinit var api: IWXAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wechat = SocialConfig.weixin ?: throw IllegalArgumentException("wechat is NULL !!!")
        api = WXAPIFactory.createWXAPI(this, wechat.appId, false)
        api.handleIntent(intent, this)
    }


    override fun onResp(response: BaseResp?) {

        Timber.d(response.toString())
        val code = response?.errCode

        //登陆返回
        if (response is SendAuth.Resp) {

        }

    }

    override fun onReq(request: BaseReq?) {
        Timber.d(request.toString())

    }
}