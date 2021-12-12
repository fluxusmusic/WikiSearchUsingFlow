package com.mc.kim.remote.parser

internal class DefaultRequestParser : RequestParser<String>() {
    override fun requestBodyParser(body: String): String {
        return body
    }
}