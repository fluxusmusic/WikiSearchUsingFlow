package com.mc.kim.remote.api


import com.mc.kim.remote.Method
import com.mc.kim.remote.parser.RequestParser
import com.mc.kim.remote.parser.ResponseParser
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.net.MalformedURLException
import java.net.URISyntaxException

internal open class PutApi(baseUrl: String, private val path: String) :
    Requester(baseUrl) {

    @Deprecated("can not use it on PUT")
    override fun setQuery(queryValue: QueryValue) {
    }

    @Deprecated("can not use it on PUT")
    override fun setQuery(queryList: List<QueryValue>) {
    }

    private var _body: String? = null
    override fun <T> setBody(body: T, requestParser: RequestParser<T>) {
        _body = requestParser.requestBodyParser(body)
    }

    override fun method(): Method = Method.PUT
    override fun path(): String {
        return path
    }

    override fun <T> connect(responseParser: ResponseParser<T>): ResponseResult<T> {
        return try {
            val urlConnection = super.openConnection().apply {
                doOutput = true
                val os = getOutputStream()
                os.write(_body?.encodeToByteArray())
                os.close()
            }
            try {
                return makeResult(urlConnection, responseParser)
            } finally {
                disconnectConnection(urlConnection)
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            ResponseResult.Fail(IllegalArgumentException("Wrong url format, please check"))
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            ResponseResult.Fail(Exception("Wrong url format, please check"))
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            ResponseResult.Fail(MalformedURLException("Wrong url format, please check"))
        } catch (e: IOException) {
            e.printStackTrace()
            ResponseResult.Fail(IOException("Please check network"))
        }
    }
}