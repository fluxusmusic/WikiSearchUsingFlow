package com.mc.kim.remote

import com.mc.kim.remote.api.Requester
import com.mc.kim.remote.manager.SimpleDataManager
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test

class HttpSimpleDataManagerUnitTest  : TestCase(){
    private val TAG = HttpSimpleDataManagerUnitTest::class.simpleName
    private var mockServer: MockWebServer = MockWebServer()
    @Before
    fun startServer() {
        mockServer.start(8080)

        val dispatcher: Dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                request.print()
                return MockResponse().setResponseCode(200).setBody("Success")
            }
        }
        mockServer.dispatcher = dispatcher
    }

    @After
    fun stopServer() {
        this.mockServer.shutdown()
    }

    @Test
    internal fun startGetTest() {
        var queryList = arrayListOf<Requester.QueryValue>()
        queryList.add(Requester.QueryValue("keyword", "google"))
        queryList.add(Requester.QueryValue("page", 1))
        var result = SimpleDataManager(
            Method.GET,
            "http://${mockServer.hostName}:${mockServer.port}"
        ).getRequester("test").apply {
            connectTimeOut = CONNECT_TIME_OUT
            readTimeOut = READ_TIME_OUT
            addHeader(Requester.HeaderValue("Connection","Close"))
            addHeader(Requester.HeaderValue("Accept","application/json"))
            setQuery(queryList)
        }.connect()

        println("startGetTest : result : ${result}")
    }

    @Test
    internal fun startPostTest() {
        var result = SimpleDataManager(
            Method.POST,
            "http://${mockServer.hostName}:${mockServer.port}"
        ).getRequester("test").apply {
            connectTimeOut = CONNECT_TIME_OUT
            readTimeOut = READ_TIME_OUT
            addHeader(Requester.HeaderValue("Connection","Close"))
            addHeader(Requester.HeaderValue("Content-Type","application/json"))
            addHeader(Requester.HeaderValue("Accept","application/json"))
            setBody(dummyBodyString)
        }.connect()
        println("startPostTest result : ${result}")
    }

    @Test
    internal fun startDeleteTest() {
        var queryList = arrayListOf<Requester.QueryValue>()
        queryList.add(Requester.QueryValue("keyword", "google"))
        queryList.add(Requester.QueryValue("page", 1))
        var result = SimpleDataManager(
            Method.DELETE,
            "http://${mockServer.hostName}:${mockServer.port}"
        ).getRequester("test").apply {
            connectTimeOut = CONNECT_TIME_OUT
            readTimeOut = READ_TIME_OUT
            addHeader(Requester.HeaderValue("Connection","Close"))
            addHeader(Requester.HeaderValue("Accept","application/json"))
            setQuery(queryList)
        }. connect()

        println("startGetTest : result : ${result}")
    }

    @Test
    internal fun startPutTest() {
        var result = SimpleDataManager(
            Method.PUT,
            "http://${mockServer.hostName}:${mockServer.port}"
        ).getRequester("test").apply {
            connectTimeOut = CONNECT_TIME_OUT
            readTimeOut = READ_TIME_OUT
            addHeader(Requester.HeaderValue("Connection","Close"))
            addHeader(Requester.HeaderValue("Content-Type","application/json"))
            addHeader(Requester.HeaderValue("Accept","application/json"))
            setBody(dummyBodyString)
        }.connect()
        println("startPostTest result : ${result}")
    }

    private fun RecordedRequest.print() {
        println("RecordedRequest | method : ${this.method}")
        println("RecordedRequest | path : ${this.path}")
        println("RecordedRequest | body : ${this.body}")
        println("RecordedRequest | header : ${this.headers}")
    }
}