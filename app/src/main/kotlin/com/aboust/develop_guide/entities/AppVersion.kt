package com.aboust.develop_guide.entities

import java.util.*


data class AppVersion(val versionCode: Int,
                      val versionName: String,
                      val uploadTime: Date,
                      val content: String,
                      val downloadUrl: String)