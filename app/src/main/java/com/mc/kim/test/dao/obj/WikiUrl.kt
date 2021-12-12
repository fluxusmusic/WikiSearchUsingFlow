package com.mc.kim.test.dao.obj

import android.os.Parcelable
import com.mc.kim.remote.annotation.JsonValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class WikiUrl(
    @JsonValue("page")
    val page: String,
    @JsonValue("revisions")
    val revisions: String,
    @JsonValue("edit")
    val edit: String,
    @JsonValue("talk")
    val talk: String
) : Parcelable