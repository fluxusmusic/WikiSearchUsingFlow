package com.mc.kim.test.ui.fragment.viewstate

import com.mc.kim.remote.api.ResponseResult
import com.mc.kim.test.dao.response.WikiData
import com.mc.kim.test.dao.response.WikiDataList

sealed class WikiState {
    object Idle : WikiState()
    object Loading :WikiState()
    data class ResultData(val result: ResponseResult<WikiData>) : WikiState()
    data class ResultDataList(val result: ResponseResult<WikiDataList>) : WikiState()
}