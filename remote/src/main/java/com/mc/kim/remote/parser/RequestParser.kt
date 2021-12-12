package com.mc.kim.remote.parser

/**
 *
 * It is the request data parser. Only for [Method.POST], [Method.PUT]
 *
 * @param T Class Type
 *
 */
abstract class RequestParser<T> {
    abstract fun requestBodyParser(body: T): String
}