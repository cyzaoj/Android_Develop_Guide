package com.aboust.develop_guide.service.launch

import android.app.Application
import android.widget.Toast
import com.aboust.develop_guide.BuildConfig
import com.aboust.develop_guide.LOGGER_TAG
import com.aboust.develop_guide.kit.initjob.Job
import com.osama.firecrasher.CrashListener
import com.osama.firecrasher.FireCrasher
import timber.log.Timber
import timber.log.Timber.DebugTree


/**
 * app 启动初始化
 */
class AppStarter(private val application: Application) : Job() {


    override fun run() {
        Timber.tag(LOGGER_TAG)
        Timber.plant(if (BuildConfig.DEBUG) DebugTree() else CrashReportingTree())
        FireCrasher.install(application, object : CrashListener() {
            override fun onCrash(throwable: Throwable) {
                Toast.makeText(application, throwable.message, Toast.LENGTH_SHORT).show()
                recover()
                // Ex: Crashlytics.logException(throwable);
            }
        })
    }

    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {}
    }
}