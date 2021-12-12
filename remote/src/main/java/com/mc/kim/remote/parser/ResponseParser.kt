package com.mc.kim.remote.parser



/**
 *
 * It is the response data parser.
 * If you declare the parser when using the requester, you can get as format.
 * not declared, return type is String
 * @param T Class Type for response data
 *
 */
abstract class ResponseParser<T> {
    abstract fun responseBodyParser(body: String): T
}