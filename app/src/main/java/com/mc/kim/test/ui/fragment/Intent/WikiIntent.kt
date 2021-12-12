package com.mc.kim.test.ui.fragment.Intent

import com.mc.kim.test.dao.response.WikiData

sealed class WikiIntent {
    data class FetchData(val keyword:String): WikiIntent()
    data class FetchDataList(val wikiData: WikiData) : WikiIntent()
}