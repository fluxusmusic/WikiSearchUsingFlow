package com.mc.kim.remote.api

import com.mc.kim.remote.CONNECT_TIME_OUT
import com.mc.kim.remote.Method
import com.mc.kim.remote.READ_TIME_OUT
import com.mc.kim.remote.parser.DefaultRequestParser
import com.mc.kim.remote.parser.DefaultResponseParser
import com.mc.kim.remote.parser.RequestParser
import com.mc.kim.remote.parser.ResponseParser
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.net.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


sealed class ResponseResult<out R> {

    /**
     * when api call success return ResponseResult.Success
     * @property data return data
     */
    data class Success<out T>(val data: T) : ResponseResult<T>()

    /**
     * If an error occurs when calling the API, it returns ResponseResult.Error
     * @property errorCode : http responseCode
     * @property errorMessage : http responseMessage
     */
    data class Error(val errorCode: Int, val errorMessage: String) : ResponseResult<Nothing>()

    /**
     * If can not call API, it returns ResponseResult.Fail
     * @property exception : Network not connected, wrong URL format, etc...
     */
    data class Fail(val exception: Exception) : ResponseResult<Nothing>()

}

abstract class Requester(private val baseUrl: String) {
    data class HeaderValue(val key: String, val value: String)
    data class QueryValue(val key: String, val value: Any)

    var readTimeOut = READ_TIME_OUT
    var connectTimeOut = CONNECT_TIME_OUT
    private var _headerList: ArrayList<HeaderValue> = arrayListOf()

    /**
     * set Request Body Data, only can use [Method.POST], [Method.PUT]
     * @param body : String
     */
    fun setBody(body: String) {
        setBody(body, DefaultRequestParser())
    }

    /**
     * set Request Body Data, only can use [Method.POST], [Method.PUT]
     * @param body : Custom Body Type
     * @param requestParser : parser for custom body type : [RequestParser]
     */
    abstract fun <T> setBody(body: T, requestParser: RequestParser<T>)

    /**
     * If call connect method, [Requester] connect server for get response
     * @property ResponseResult default return type is String
     */
    fun connect(): ResponseResult<String> {
        return connect(DefaultResponseParser())
    }

    /**
     * If call connect method, [Requester] connect server for get response
     * @param responseParser : parser for custom request type : [RequestParser]
     * @property ResponseResult : up to responseParser type
     */
    abstract fun <T> connect(responseParser: ResponseParser<T>): ResponseResult<T>

    /**
     * set Request Query parameter Data, only can use [Method.GET], [Method.DELETE]
     * @param queryValue : [QueryValue] key, value
     */
    abstract fun setQuery(queryValue: QueryValue)

    /**
     * set Request Query parameter Data, only can use [Method.GET], [Method.DELETE]
     * @param queryList : List [QueryValue] key, value
     */
    abstract fun setQuery(queryList: List<QueryValue>)

    /**
     * add Request HTTP Header
     * @param header : [HeaderValue] key, value
     */
    fun addHeader(header: HeaderValue) {
        _headerList.add(header)
    }

    /**
     * add Request HTTP Header List
     * @param headerList : List [HeaderValue] key, value
     */
    fun addHeaderList(headerList: List<HeaderValue>) {
        _headerList.addAll(headerList)
    }

    internal abstract fun method(): Method
    internal abstract fun path(): String

    private fun header(): ArrayList<HeaderValue> {
        if (_headerList.isEmpty()) {
            _headerList.add(HeaderValue("Connection", "close"))
            _headerList.add(HeaderValue("Accept", "text/plain"))
        }
        return _headerList
    }

    private var _sslSocketFactory: SSLSocketFactory? = null

    private fun getSSLSocket(): SSLSocketFactory {
        if (_sslSocketFactory == null) {
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )
            val sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, trustAllCerts, SecureRandom())
            return sslContext.socketFactory
        }
        return _sslSocketFactory as SSLSocketFactory
    }

     fun setSSLSocketFactory(sslSocketFactory: SSLSocketFactory){
        this._sslSocketFactory = sslSocketFactory
    }


    @Throws(
        IOException::class,
        MalformedURLException::class,
        URISyntaxException::class,
        IllegalArgumentException::class
    )
    protected fun openConnection(): URLConnection {

        val address = "$baseUrl/${path()}"
        val uri: URI = URI.create(address)
        val url = uri.toURL()
        return url.openConnection().apply {
            if (this is HttpURLConnection) {
                requestMethod = method().methodName
            } else if (this is HttpsURLConnection) {
                requestMethod = method().methodName
                sslSocketFactory = getSSLSocket()
            }

            for (headerValue in header()) {
                setRequestProperty(headerValue.key, headerValue.value)
            }
            doInput = true

            this.readTimeout = readTimeOut
            this.connectTimeout = connectTimeOut
        }
    }


    protected fun List<QueryValue>.toQueryString(): String {
        if (isEmpty()) {
            return ""
        }

        var queryBuilder = StringBuilder()
        for ((index, queryValue) in withIndex()) {
            if (index == 0) {
                queryBuilder.append("?")
            }
            queryBuilder.append("${queryValue.key}=${queryValue.value}")
            if (index != lastIndex) {
                queryBuilder.append("&")
            }
        }
        return queryBuilder.toString()
    }

    private fun readStream(inputStream: InputStream): String {
        val contents = ByteArray(1024)
        var bytesRead = 0
        val sb = StringBuilder()
        while (inputStream.read(contents).also { bytesRead = it } != -1) {
            sb.append(String(contents, 0, bytesRead))
        }
        return sb.toString()
    }

    internal fun <T> makeResult(
        urlConnection: URLConnection,
        responseParser: ResponseParser<T>
    ): ResponseResult<T> =
        when (urlConnection) {
            is HttpsURLConnection -> {
                if (urlConnection.responseCode == HttpsURLConnection.HTTP_OK) {
                    val inputStream: InputStream =
                        BufferedInputStream(urlConnection.getInputStream())
                    try{
                        ResponseResult.Success(responseParser.responseBodyParser(readStream(inputStream)))
                    }catch (e:Exception){
                        ResponseResult.Fail(Exception("exception occurred during the parsing, please check"))
                    }

                } else {
                    ResponseResult.Error(urlConnection.responseCode, urlConnection.responseMessage)
                }
            }
            is HttpURLConnection -> {
                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream: InputStream =
                        BufferedInputStream(urlConnection.getInputStream())
                    try{
                        ResponseResult.Success(responseParser.responseBodyParser(readStream(inputStream)))
                    }catch (e:Exception){
                        ResponseResult.Fail(Exception("exception occurred during the parsing, please check"))
                    }
                } else {
                    ResponseResult.Error(urlConnection.responseCode, urlConnection.responseMessage)
                }
            }
            else -> {
                ResponseResult.Fail(Exception("Cannot connect server, please check URL"))
            }
        }


    internal fun disconnectConnection(urlConnection: URLConnection) {
        if (urlConnection is HttpURLConnection) {
            urlConnection.disconnect()
        } else if (urlConnection is HttpsURLConnection) {
            urlConnection.disconnect()
        }
    }


}


