package com.aboust.develop_guide.service

class OnDownload {

    var debug = false

    var params: MutableMap<String, Any?> = HashMap()

    var url: String? = null

    fun debug(debug: Boolean) {
        this.debug = debug
    }

    fun url(url: String) {
        this.url = url
    }


    fun params(key: String, value: Any?) {
        this.params[key] = value
    }


//    private var ossApi: OssApi
//
//    init {
//        val fetch = Fetch.INSTANCE
//        val retrofit: Retrofit = fetch.retrofit(OSS_HOST)
//        ossApi = fetch.service(retrofit, OssApi::class.java)
//    }
//
//    fun app() = GlobalScope.launch(Dispatchers.IO) {
//        val result = ossApi.appVersion().await()
//        Timber.d(result.toString())
//    }


}

interface AppInstall {

}