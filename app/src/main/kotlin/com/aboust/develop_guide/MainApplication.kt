package com.aboust.develop_guide

import android.app.NotificationManager
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.aboust.develop_guide.kit.network.Fetch
import com.aboust.develop_guide.kit.notify.Notify
import com.aboust.develop_guide.kit.notify.entities.NotifyChannel
import com.aboust.develop_guide.widget.initjob.JobLaunch
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
        Fetch.INSTANCE.create(this)
        JobLaunch.init(this)
        JobLaunch.newInstance().run {

            start()
            await()
        }
        Notify.config {
            channels {
                plus(NotifyChannel("1", "IMPORTANCE_MIN", 1))
                plus(NotifyChannel("3", "IMPORTANCE_DEFAULT", 3))
            }
            header {
                icon = R.mipmap.ic_launcher
                name = getString(R.string.app_name)
                color = R.color.colorPrimary
                showWhen = true
            }
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
    }
}