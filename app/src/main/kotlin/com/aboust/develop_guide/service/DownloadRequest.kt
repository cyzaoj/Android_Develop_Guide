package com.aboust.develop_guide.service

import android.net.Uri
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Pair


/**
 * 下载任务请求
 */
class DownloadRequest private constructor() : Parcelable {
    private var allowedNetworkType: Int = -1

    private var uri: Uri? = null

    private var destinationUri: Uri? = null

    private var headers: HashMap<String, String> = HashMap()

    private var title: CharSequence? = null

    private var description: CharSequence? = null

    private var mimeType: String? = null

    private var hasNotification: Boolean = false

    constructor(uri: Uri) : this() {
        val scheme = uri.scheme
        require(!(scheme == null || scheme != "http" && scheme != "https"))
        { "Can only download HTTP/HTTPS URIs: $uri" }
        this.uri = uri
    }

    constructor(uriString: String) : this() {
        this.uri = Uri.parse(uriString)
    }

    fun destinationUri(destinationUri: Uri): DownloadRequest {
        this.destinationUri = destinationUri
        return this
    }

    fun uri() = this.uri

    fun title() = this.title

    fun headers() = this.headers

    fun description() = this.description

    fun destinationUri() = this.destinationUri

    fun mimeType() = this.mimeType

    fun allowedNetworkType() = this.allowedNetworkType

    fun hasNotification() = this.hasNotification

    fun uri(uri: Uri): DownloadRequest {
        this.uri = uri
        return this
    }

    fun allowedNetworkType(allowedNetworkType: Int): DownloadRequest {
        this.allowedNetworkType = allowedNetworkType
        return this
    }

    fun addHeader(key: String, value: String): DownloadRequest {
        require(!key.contains(":")) { "header may not contain ':'" }
        headers[key] = value
        return this
    }

    fun title(title: CharSequence): DownloadRequest {
        this.title = title
        return this
    }

    fun description(description: CharSequence): DownloadRequest {
        this.description = description
        return this
    }

    fun hasNotification(hasNotification: Boolean): DownloadRequest {
        this.hasNotification = hasNotification
        return this
    }

    fun mimeType(mimeType: String): DownloadRequest {
        this.mimeType = mimeType
        return this
    }

    constructor(source: Parcel) : this() {
        this.allowedNetworkType = source.readInt()
        this.uri = source.readParcelable(Uri::class.java.classLoader)
        this.destinationUri = source.readParcelable(Uri::class.java.classLoader)
//        this.headers = source.readHashMap(HashMap::) as HashMap<String, String>
        this.title = source.readString()
        this.description = source.readString()
        this.mimeType = source.readString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.hasNotification = source.readBoolean()
        } else {
            this.hasNotification = 1 == source.readInt()
        }
    }


    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        dest.writeInt(allowedNetworkType())
        dest.writeParcelable(uri(), flags)
        dest.writeParcelable(destinationUri(), flags)
//        dest.writeTypedArrayMap(headers(),flags)
        dest.writeString(title() as String?)
        dest.writeString(description() as String?)
        dest.writeString(mimeType())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(hasNotification())
        } else {
            val f = if (hasNotification()) 1 else 0
            dest.writeInt(f)
        }

    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DownloadRequest> = object : Parcelable.Creator<DownloadRequest> {
            override fun createFromParcel(source: Parcel): DownloadRequest = DownloadRequest(source)
            override fun newArray(size: Int): Array<DownloadRequest?> = arrayOfNulls(size)
        }
    }
}