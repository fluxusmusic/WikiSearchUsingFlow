package com.mc.kim.test.ui.fragment.row

import com.mc.kim.test.dao.response.WikiData
import com.mc.kim.test.ui.fragment.loader.ResourceLoader

sealed class WikiRow(val type: RowType) {

    data class HeaderWikiRow(val wikiData: WikiData, val resourceLoader: ResourceLoader) :
        WikiRow(RowType.Header)

    data class RelatedWikiRow(val wikiData: WikiData, val resourceLoader: ResourceLoader) :
        WikiRow(RowType.Item)

    enum class RowType(val viewType: Int) {
        Header(0), Item(1)
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(wikiData: WikiData, type:RowType)
    }
}