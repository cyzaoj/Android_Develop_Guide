package com.aboust.develop_guide.kit.fetch

import android.content.Context
import com.aboust.develop_guide.kit.fetch.Environment.Companion.getSelectedEnvironment
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HostInterceptor(context: Context) : Interceptor {

    private val preferences = Environment.preferences(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val environment = preferences.getSelectedEnvironment()
        val newRequest = when {
            environment == Environment.None -> request
            request.headers[SKIP_HEADER_NAME]?.toBoolean() == true -> request
            else -> request.host(environment.hosts)
        }.removeSkipHeader()
        return chain.proceed(newRequest)
    }

    private fun Request.removeSkipHeader(): Request {
        return newBuilder().removeHeader(SKIP_HEADER_NAME).build()
    }

    private fun Request.host(hostString: String): Request {

        //如果 Url 地址中包含 IDENTIFICATION_IGNORE 标识符, 框架将不会对此 Url 进行任何切换 BaseUrl 的操作
        if (hostString.contains(IDENTIFICATION_IGNORE)) {
            return pruneIdentification(newBuilder, url);
        }

        val host = hostString.toHttpUrl()
        val newUrl = url.newBuilder()
                .scheme(host.scheme)
                .host(host.toUrl().host)
                .port(host.port)
                .build()
        return newBuilder().url(newUrl).build()
    }

    /**
     * 对 {@link Request} 进行一些必要的加工, 执行切换 BaseUrl 的相关逻辑
     *
     * @param request {@link Request}
     * @return {@link Request}
     */
    public fun processRequest(request: Request): Request {

        val newBuilder = request.newBuilder()

        val url = request.url().toString()
        //如果 Url 地址中包含 IDENTIFICATION_IGNORE 标识符, 框架将不会对此 Url 进行任何切换 BaseUrl 的操作
        if (url.contains(IDENTIFICATION_IGNORE)) {
            return pruneIdentification(newBuilder, url)
        }

        val domainName: String? = obtainDomainNameFromHeaders(request)

    }

    /**
     * 从 [Request.header] 中取出 DomainName
     *
     * @param request [Request]
     * @return DomainName
     */
    private fun obtainDomainNameFromHeaders(request: Request): String? {
        val headers = request.headers(DOMAIN_NAME)
        if (headers.isEmpty()) return null
        require(headers.size <= 1) { "Only one Domain-Name in the headers" }
        return request.header(DOMAIN_NAME)
    }

    /**
     * 将 `IDENTIFICATION_IGNORE` 从 Url 地址中修剪掉
     *
     * @param newBuilder [Request.Builder]
     * @param url        原始 Url 地址
     * @return 被修剪过 Url 地址的 [Request]
     */
    private fun pruneIdentification(newBuilder: Request.Builder, url: String): Request {
        val split = url.split(IDENTIFICATION_IGNORE).toTypedArray()
        val buffer = StringBuffer()
        for (s in split) {
            buffer.append(s)
        }
        return newBuilder.url(buffer.toString()).build()
    }


    companion object {
        private const val SKIP_HEADER_NAME = "Skip-Host-Interception"
        private const val SKIP_HEADER = "$SKIP_HEADER_NAME: true"

        /**
         * 如果在 Url 地址中加入此标识符, 框架将不会对此 Url 进行任何切换 BaseUrl 的操作
         */
        const val IDENTIFICATION_IGNORE = "#url_ignore"

        private const val DOMAIN_NAME = "Domain-Name"
        const val DOMAIN_NAME_HEADER = "$DOMAIN_NAME: "


    }
}