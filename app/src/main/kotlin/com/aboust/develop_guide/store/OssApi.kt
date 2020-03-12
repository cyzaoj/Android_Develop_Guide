package com.aboust.develop_guide.store

import com.aboust.develop_guide.entities.AppVersion
import retrofit2.Call
import retrofit2.http.GET


interface OssApi {


    @GET("/dev/update_apk.json")
    fun appVersion(): Call<AppVersion?>

}