package com.mc.kim.test.dao

import com.mc.kim.remote.Method
import com.mc.kim.remote.api.Requester
import com.mc.kim.remote.api.ResponseResult
import com.mc.kim.remote.manager.SimpleDataManager
import com.mc.kim.remote.parser.ResponseParser
import com.mc.kim.remote.util.JsonParser
import com.mc.kim.test.dao.response.WikiData
import com.mc.kim.test.dao.response.WikiDataList

private const val WIKI_BASE_URL = "https://en.wikipedia.org/api/rest_v1/page"

class WikiDataManager : SimpleDataManager(Method.GET, WIKI_BASE_URL) {
    private val PATH_SUMMARY = "summary"
    private val PATH_RELATED = "related"
    private val WIKI_readTimeOut = 3000
    private val WIKI_connectTimeOut = 3000


    private fun getSummaryKeyword(keyword: String): Requester {
        return super.getRequester("${PATH_SUMMARY}/$keyword").apply {
            this.readTimeOut = WIKI_readTimeOut
            this.connectTimeOut = WIKI_connectTimeOut
        }
    }

    private fun getRelatedKeyword(keyword: String): Requester {
        return super.getRequester("${PATH_RELATED}/$keyword").apply {
            this.readTimeOut = WIKI_readTimeOut
            this.connectTimeOut = WIKI_connectTimeOut
        }
    }

    fun requestSummary(keyword: String): ResponseResult<WikiData> = getSummaryKeyword(keyword)
        .connect(object : ResponseParser<WikiData>() {
            override fun responseBodyParser(body: String): WikiData {
                return JsonParser().toJson(body, WikiData::class)
            }
        })

    fun requestRelatedLit(wikiData: WikiData): ResponseResult<WikiDataList> =
        getRelatedKeyword(wikiData.titles.canonical).connect(object : ResponseParser<WikiDataList>() {
            override fun responseBodyParser(body: String): WikiDataList {
                return JsonParser().toJson(body, WikiDataList::class)
            }
        })
}