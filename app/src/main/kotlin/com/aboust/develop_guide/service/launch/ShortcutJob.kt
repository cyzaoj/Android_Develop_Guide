package com.aboust.develop_guide.service.launch

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.content.ContextCompat
import com.aboust.develop_guide.R
import com.aboust.develop_guide.kit.initjob.Job
import com.aboust.develop_guide.ui.activity.UmbrellaActivity
import timber.log.Timber



class ShortcutJob(private val context: Context) : Job() {


    //是否需要在阻塞在await(),在Application的onCreate方法之前执行完
    override fun needRunAsSoon(): Boolean = false

    //是否需要运行在主线程
    override fun onMainThread(): Boolean = false
    override fun run() {

        addShortcut()
    }


    private fun addShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutManager = ContextCompat.getSystemService(context, ShortcutManager::class.java)
            val maxShortcutCountPerActivity = shortcutManager?.maxShortcutCountPerActivity ?: 0
            Timber.d("maxShortcutCountPerActivity => %s", maxShortcutCountPerActivity)
            val dynamicShortcuts: MutableList<ShortcutInfo> = ArrayList()
            val i = Intent(context, UmbrellaActivity::class.java)
            i.action = Intent.ACTION_VIEW
            dynamicShortcuts.add(
                    ShortcutInfo.Builder(context, "UmbrellaActivity").setShortLabel(context.getString(R.string.umbrella))
                            .setLongLabel(context.getString(R.string.umbrella))
                            .setIntent(i)
                            .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher)).build()
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

}