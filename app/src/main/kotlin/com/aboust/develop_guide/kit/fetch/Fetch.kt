package com.aboust.develop_guide.kit.fetch

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.aboust.develop_guide.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit


/**
 * 网络请求
 */
open class Fetch private constructor() {

    private var debug: Boolean = false

    private var context: Context? = null

    private var headers: MutableMap<String, Any?> = HashMap()

    companion object {
        const val TIME_OUT_CONNECT = 5000L
        const val TIME_OUT_READ = 20000L
        const val TIME_OUT_WRITE = 20000L
        const val MAX_SIZE: Long = 20 * 1024 * 1024

        val INSTANCE: Fetch by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Fetch() }
    }


    fun header(key: String, value: Any?): Fetch {
        headers[key] = value
        return this
    }

    fun create(context: Context): Fetch {
        this.context = context
        return this
    }


    fun retrofit(baseUrl: String): Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(toGson()))
            .client(okClient())
            .baseUrl(baseUrl)
            .build()


    /**
     * 获取API服务
     */
    fun <T> service(retrofit: Retrofit, service: Class<T>): T {
        return retrofit.create(service)
    }

    /**
     * >= jdk1.8 localDateTime
     *
     */
    private fun toGson(): Gson {
        val builder = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls() //输出null
                .setPrettyPrinting()//格式化输出

        //localDateTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
                val instant = Instant.ofEpochMilli(json.asJsonPrimitive.asLong)
                LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            })
        }
        return builder.create()
    }

    private fun okClient(): OkHttpClient {
        if (null == this.context) {
            throw IllegalArgumentException("Fetch create isn't execute!!!")
        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC

        val builder = OkHttpClient.Builder().connectTimeout(TIME_OUT_CONNECT, TimeUnit.MILLISECONDS)
                .readTimeout(TIME_OUT_READ, TimeUnit.MILLISECONDS)
                .writeTimeout(TIME_OUT_WRITE, TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(baseInterceptor())
                .addNetworkInterceptor(httpCacheInterceptor())
                .cache(Cache(this.context!!.cacheDir, MAX_SIZE)) // 设置缓存路径和缓存容量
        return builder.build()
    }


    /**
     * 设置头
     */
    private fun baseInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("platform", "android")
                    .addHeader("debug", this.debug.toString())
                    .method(originalRequest.method, originalRequest.body)

            for ((key, value) in headers) {
                value?.let { requestBuilder.addHeader(key, it.toString()) }
            }
            /*  val requestBuilder = originalRequest.newBuilder()
                      .addHeader("Accept", "application/json")
  //                .addHeader("Authorization", BEAT_LOGIN?.token.toSafe())
                      .addHeader("Authorization", "")
                      .addHeader("platform", "android")
                      .addHeader("package", context.packageName)
                      .addHeader("version_code", AppUtils.getAppVersionCode().toString())
                      .addHeader("version_name", AppUtils.getAppVersionName())*/
//                    .method(originalRequest.method, originalRequest.body)
            chain.proceed(requestBuilder.build())
        }
    }


    /**
     * 设置缓存
     */
    private fun httpCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request: Request = if (isNetworkConnected()) {
                chain.request()
            } else {
                //没网强制从缓存读取
                chain.request().newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            }

            val response = chain.proceed(request)
            val maxAge = if (isNetworkConnected()) 0 else 60 * 60 * 6
            val builder = response.newBuilder().removeHeader("Pragma")
                    .removeHeader("Cache-Control")
            builder.removeHeader(this.context!!.packageName)            // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .addHeader(
                            "Cache-Control",
                            if (isNetworkConnected()) "public, max-age=$maxAge" else "public, only-if-cached, max-stale=$maxAge"
                    )
                    .build()
        }
    }


    /**
     * 检测网络连接状态是否可用
     *
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    open fun isNetworkConnected(): Boolean {
        val cm = ContextCompat.getSystemService(context!!, ConnectivityManager::class.java)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val networkInfo = cm?.activeNetworkInfo ?: return false
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE)
                return true
        } else {
            val network: Network? = cm?.activeNetwork
            val nc = cm?.getNetworkCapabilities(network) ?: return false
            if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                return true
        }
        return false
    }


}