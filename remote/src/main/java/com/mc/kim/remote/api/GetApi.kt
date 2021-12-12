package com.mc.kim.remote.api

import com.mc.kim.remote.Method
import com.mc.kim.remote.parser.RequestParser
import com.mc.kim.remote.parser.ResponseParser
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.net.MalformedURLException
import java.net.URISyntaxException

internal open class GetApi(baseUrl: String, private val path: String) :
    Requester(baseUrl) {
    private val TAG = GetApi::class.simpleName!!
    private var _queryList: ArrayList<QueryValue> = arrayListOf()
    override fun setQuery(queryValue: QueryValue) {
        _queryList.clear()
        _queryList.add(queryValue)
    }

    override fun setQuery(queryList: List<QueryValue>) {
        _queryList.clear()
        _queryList.addAll(queryList)
    }

    @Deprecated("can not use it on GET")
    override fun <T> setBody(body: T, requestParser: RequestParser<T>) {
    }

    override fun method(): Method = Method.GET

    override fun path(): String {
        return "$path${_queryList.toQueryString()}"
    }

    override fun <T> connect(responseParser: ResponseParser<T>): ResponseResult<T> {
        return try {
            val urlConnection = super.openConnection()
            try {
                makeResult(urlConnection, responseParser)
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