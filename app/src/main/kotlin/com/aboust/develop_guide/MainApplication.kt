package com.aboust.develop_guide

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.multidex.MultiDexApplication
import com.aboust.develop_guide.kit.fetch.Fetch
import com.aboust.develop_guide.ui.activity.UmbrellaActivity
import com.aboust.develop_guide.widget.initjob.JobLaunch
import timber.log.Timber


class MainApplication : MultiDexApplication() {

    private lateinit var launch: JobLaunch

    override fun onCreate() {
        super.onCreate()
        JobLaunch.init(this)
        launch = JobLaunch.newInstance().apply {
            start()
            await()
        }
        configure()
        addShortcut()
    }

    private fun addShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutManager = ContextCompat.getSystemService(this, ShortcutManager::class.java)
            val maxShortcutCountPerActivity = shortcutManager?.maxShortcutCountPerActivity ?: 0
            Timber.d("maxShortcutCountPerActivity => %s", maxShortcutCountPerActivity)
            val dynamicShortcuts: MutableList<ShortcutInfo> = ArrayList()
            val i = Intent(this, UmbrellaActivity::class.java)
            i.action = Intent.ACTION_VIEW
            dynamicShortcuts.add(
                    ShortcutInfo.Builder(this, "UmbrellaActivity").setShortLabel(getString(R.string.umbrella))
                            .setLongLabel(getString(R.string.umbrella))
                            .setIntent(i)
                            .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher)).build()
            )

//            for (i in 0 until maxShortcutCountPerActivity) {
//                val info = ShortcutInfo.Builder(this, "id$i") //设置id
//                        .setShortLabel(mTitle.get(i)) //设置短标题
//                        .setLongLabel("功能:" + mTitle.get(i)) //设置长标题
//                        .setIcon(Icon.createWithResource(this, R.mipmap.xiansuo)) //设置图标
//                        .setIntent(intent) //设置intent
//                        .build()
//                dynamicShortcuts.add(info) //将新建好的shortcut添加到集合
//            }


            shortcutManager?.dynamicShortcuts = dynamicShortcuts


        }
    }


    private fun configure() {
        Fetch.INSTANCE.create(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        launch.cancel()
    }
}