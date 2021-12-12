package com.mc.kim.test.dao.response

import android.os.Parcelable
import com.mc.kim.remote.annotation.JsonValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class WikiDataList(@JsonValue("pages")
                        val pages: List<WikiData>) : Parcelable