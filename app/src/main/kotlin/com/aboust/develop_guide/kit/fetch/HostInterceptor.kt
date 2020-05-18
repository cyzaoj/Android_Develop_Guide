package com.aboust.develop_guide.kit.fetch

import android.content.Context
import android.text.TextUtils
import com.aboust.develop_guide.kit.fetch.Environment.Companion.getSelectedEnvironment
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HostInterceptor(context: Context) : Interceptor {
    private val environment: Environment
    init {
        val preferences = Environment.getSharedPreferences(context)
        this.environment = preferences.getSelectedEnvironment()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = when {
            environment == Environment.None -> request
            request.headers[SKIP_HEADER_NAME]?.toBoolean() == true -> request
            else -> processRequest(request)
        }.removeSkipHeader()
        return chain.proceed(newRequest)
    }

    private fun Request.removeSkipHeader(): Request {
        return newBuilder().removeHeader(SKIP_HEADER_NAME).build()
    }


    /**
     * 对 {@link Request} 进行一些必要的加工, 执行切换 BaseUrl 的相关逻辑
     *
     * @param request {@link Request}
     * @return {@link Request}
     */
    private fun processRequest(request: Request): Request {
        val newBuilder = request.newBuilder()
        val url = request.url.toString()
        val domainName: String? = obtainDomainNameFromHeaders(request)
        if (!TextUtils.isEmpty(domainName)) {
            val domain = this.environment.hosts[domainName]
            newBuilder.removeHeader(DOMAIN_NAME)
        }
        return newBuilder.build()
    }

    /**
     * 从 [Request.header] 中取出 DomainName
     *
     * @param request [Request]
     *
     * @return DomainName
     */
    private fun obtainDomainNameFromHeaders(request: Request): String? {
        val headers = request.headers(DOMAIN_NAME)
        if (headers.isEmpty()) return null
        require(headers.size <= 1) { "Only one Domain-Name in the headers" }
        return request.header(DOMAIN_NAME)
    }


    companion object {
        private const val SKIP_HEADER_NAME = "Skip-Host-Interception"
        private const val SKIP_HEADER = "$SKIP_HEADER_NAME: true"

        private const val DOMAIN_NAME = "Domain-Name"
        private const val DOMAIN_NAME_HEADER = "$DOMAIN_NAME: "
    }
}