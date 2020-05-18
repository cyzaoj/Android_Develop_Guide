package com.aboust.develop_guide

import androidx.multidex.MultiDexApplication
import com.aboust.develop_guide.kit.fetch.Fetch
import com.aboust.develop_guide.widget.initjob.JobLaunch

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
    }


    private fun configure() {
        Fetch.INSTANCE.create(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        launch.cancel()
    }
}