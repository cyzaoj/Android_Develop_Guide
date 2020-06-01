package com.aboust.develop_guide

import androidx.multidex.MultiDexApplication
import com.aboust.develop_guide.kit.fetch.Fetch
import com.aboust.develop_guide.kit.initjob.JobLaunch
import com.aboust.develop_guide.service.launch.FontJob
import com.aboust.develop_guide.service.launch.ShortcutJob


class MainApplication : MultiDexApplication() {

    private lateinit var launch: JobLaunch

    override fun onCreate() {
        super.onCreate()
        JobLaunch.init(this)
        launch = JobLaunch.newInstance().apply {
            val self = this@MainApplication
            add(ShortcutJob(self))
            add(FontJob(self))
            start()
            await()
        }
        Fetch.INSTANCE.create(this)

    }


    override fun onTerminate() {
        super.onTerminate()
        launch.cancel()
    }
}