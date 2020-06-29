package com.aboust.develop_guide.kit.fetch

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull


class HostSwitch private constructor() {

    private val domains: MutableMap<String, HttpUrl?> = HashMap()

    companion object {
        val INSTANCE: HostSwitch by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { HostSwitch() }
    }

    fun put(name: String, url: String) = synchronized(domains) { domains.put(name, url.toHttpUrlOrNull()) }

    @Synchronized
    fun get(name: String): HttpUrl? = domains[name]
}

fun String.toHttpUrl() = HostSwitch.INSTANCE.get(this)
