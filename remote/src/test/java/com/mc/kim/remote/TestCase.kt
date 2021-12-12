package com.mc.kim.remote

import java.security.cert.X509Certificate
import com.mc.kim.remote.annotation.JsonValue
import java.security.SecureRandom
import javax.net.ssl.*


open class TestCase {

    protected val dummyBodyObject = KeywordObj("google",1)

    protected val dummyBodyString = "{\n" +
            "\t\"keyword\": \"google\",\n" +
            "\t\"page\": 1\n" +
            "}"

    protected val dummyResult = "{\n" +
            "\t\"result\": \"success\"\n" +
            "}"

    protected fun getSSLSocketFactory() : SSLSocketFactory {
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

    protected data class SimpleResult(@JsonValue("result") val result: String)

    protected data class KeywordObj(
        @JsonValue("keyword") val keyword: String,
        @JsonValue("page") val page: Int
    )

}