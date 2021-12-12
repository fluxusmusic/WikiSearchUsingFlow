package com.mc.kim.remote.manager

import com.mc.kim.remote.Method
import com.mc.kim.remote.api.*

/**
 * Requester generator for Api calling
 * @param method : [Method.GET], [Method.POST],[Method.PUT],[Method.DELETE]
 * @param baseUrl : base url, without parameter, path
 */
open class SimpleDataManager(private val method: Method, private val baseUrl: String) {
    fun getRequester(path: String): Requester {
        return when (method) {
            Method.GET -> {
                GetApi(baseUrl, path)
            }

            Method.POST -> {
                PostApi(baseUrl, path)
            }

            Method.PUT -> {
                PutApi(baseUrl, path)
            }

            Method.DELETE -> {
                DeleteApi(baseUrl, path)
            }
        }
    }
}