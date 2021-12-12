package com.mc.kim.remote

/**
 * HTTP Method : GET, POST, PUT, DELETE Supported
 */
enum class Method(val methodName:String) {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");
}