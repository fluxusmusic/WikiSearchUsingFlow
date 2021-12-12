package com.mc.kim.test.dao.obj

import android.os.Parcelable
import com.mc.kim.remote.annotation.JsonValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    @JsonValue("source")
    val source: String,
    @JsonValue("width")
    val width: Int,
    @JsonValue("height")
    val height: Int) : Parcelable