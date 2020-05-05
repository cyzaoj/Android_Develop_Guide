package com.aboust.develop_guide.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.aboust.develop_guide.R
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.downloader.utils.Utils
import java.io.File


const val DOWNLOAD_NAME = "App_Download"

/**
 * app升级服务
 */
class DownloadService : IntentService(DOWNLOAD_NAME) {

    private lateinit var notifier: DownloadNotifier

    /**
     * 根据请求下载
     *
     */
    private fun download(request: DownloadRequest?) {
        val destinationUri = request?.destinationUri()
        val uri = request?.uri()
        val destinationPath = destinationUri?.path
        if (null === destinationPath) throw IllegalArgumentException("destinationUri is no avail")
        val targetFile = File(destinationPath)
        //        if (!dir.exists()) if (!dir.createNewFile()) {
        //            throw IllegalArgumentException("destinationUri create failure !!!")
        //        }
        val dir = if (targetFile.exists()) {
            targetFile.parentFile
        } else {
            throw IllegalArgumentException("destinationUri must is file uri")
        }
        val fileName = targetFile.name
        val dUrl = uri.toString()
        val notifierID = Utils.getUniqueId(dUrl, dir.path, fileName)

        PRDownloader.download(uri.toString(), dir.path, fileName)
                .setTag(DOWNLOAD_NAME)
                .build()
                .setOnStartOrResumeListener {
                    notifier.create(notifierID, R.mipmap.ic_launcher, request.title(), request.description())
                }
                .setOnProgressListener {
                    notifier.update(it.totalBytes, it.currentBytes)
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        notifier.stop(R.mipmap.ic_launcher, request.title())
                    }

                    override fun onError(error: Error?) {
                        notifier.stop(R.mipmap.ic_launcher, request.title())
                    }
                })
    }

    override fun onHandleIntent(intent: Intent?) {
        val bundle = intent?.extras
        val request: DownloadRequest? = bundle?.getParcelable(DOWNLOAD_REQUEST) as DownloadRequest?
        download(request)
    }


    override fun onCreate() {
        super.onCreate()
        notifier = DownloadNotifier(this.applicationContext)
        PRDownloader.initialize(this, PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(false)
                .setReadTimeout(DOWNLOAD_READ_TIME_OUT)
                .setConnectTimeout(DOWNLOAD_CONNECT_TIME_OUT)
                .setUserAgent(this.packageName)
                .build())
    }

    companion object {
        private const val DOWNLOAD_REQUEST = "download_request"
        private const val DOWNLOAD_READ_TIME_OUT = 30000
        private const val DOWNLOAD_CONNECT_TIME_OUT = 30000

        fun start(context: Context, request: DownloadRequest) {
            val i = Intent(context, DownloadService::class.java)
            i.putExtra(DOWNLOAD_REQUEST, request)
            context.startService(i)
        }
    }

}