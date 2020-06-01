package com.aboust.develop_guide.kit.typeface

import com.aboust.develop_guide.R
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.ITypeface
import java.util.*

object SiTypeface : ITypeface {


    override val author: String
        get() = "Android_Develop_Guide"

    override val characters: Map<String, Char> by lazy {
        Icon.values().associate { it.name to it.character }
    }

    override val description: String
        get() = "Android_Develop_Guide ITypeface"
    override val fontName: String
        get() = "Si"
    override val fontRes: Int
        get() = R.font.iconfont
    override val iconCount: Int
        get() = characters.size
    override val icons: List<String>
        get() = characters.keys.toCollection(LinkedList())

    override val license: String
        get() = ""
    override val licenseUrl: String
        get() = ""
    override val mappingPrefix: String
        get() = "si"
    override val url: String
        get() = ""
    override val version: String
        get() = "1.0.0"

    override fun getIcon(key: String): IIcon = Icon.valueOf(key)


    enum class Icon constructor(override val character: Char) : IIcon {
        si_zhifubao1('\ue637'),
        si_wechat('\ue619'),
        si_zhifubaozhanghu('\ue60b'),
        si_weixin('\ue69d'),
        si_saoma('\ue62c'),

        ;

        override val typeface: ITypeface by lazy { SiTypeface }
    }
}