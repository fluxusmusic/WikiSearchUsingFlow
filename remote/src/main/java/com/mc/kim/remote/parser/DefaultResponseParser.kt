package com.mc.kim.remote.parser

internal class DefaultResponseParser : ResponseParser<String>() {
    override fun responseBodyParser(body: String): String {
        return body
    }
}