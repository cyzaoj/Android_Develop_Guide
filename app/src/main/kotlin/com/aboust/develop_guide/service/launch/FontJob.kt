package com.aboust.develop_guide.service.launch

import android.content.Context
import com.aboust.develop_guide.kit.initjob.Job
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.typeface.GenericFont
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic

class FontJob(private val context: Context) : Job() {

    //是否需要在阻塞在await(),在Application的onCreate方法之前执行完
    override fun needRunAsSoon(): Boolean = false

    //是否需要运行在主线程
    override fun onMainThread(): Boolean = false

    override fun run() {
//        Iconics.registerFont(SiTypeface)

        Iconics.registerFont(MaterialDesignIconic)
        //Generic font creation process
        GenericFont("ADG", "adg", "adg", "fonts/iconfont.ttf")
                .also {
                    it.registerIcon("zhifubao_default", '\ue637')
                    it.registerIcon("wechat", '\ue619')
                    it.registerIcon("weixin", '\ue69d')
                    it.registerIcon("saoma", '\ue62c')
                    it.registerIcon("zhifubao", '\ue60b')
                    Iconics.registerFont(it)
                }


    }
}