package com.aboust.develop_guide

import androidx.multidex.MultiDexApplication
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic

class MainApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        configure()
    }


    private fun configure() {
        Iconics.init(applicationContext)
        Iconics.registerFont(MaterialDesignIconic)
    }
}