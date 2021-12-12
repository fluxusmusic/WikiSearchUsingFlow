package com.mc.kim.test.ui.fragment.viewstate

import com.mc.kim.remote.api.ResponseResult
import com.mc.kim.test.dao.response.WikiData
import com.mc.kim.test.dao.response.WikiDataList

sealed class WikiState {
    object Idle : WikiState()
    object Loading : WikiState()
    data class ResultData(val result: ResponseResult.Success<WikiData>) : WikiState()
    data class ResultDataList(val result: ResponseResult.Success<WikiDataList>) : WikiState()

    data class Error(val error: ResponseResult.Error) : WikiState()
    data class Fail(val fail: ResponseResult.Fail) : WikiState()
}