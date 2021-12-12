package com.mc.kim.test.dao.obj

import android.os.Parcelable
import com.mc.kim.remote.annotation.JsonValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class NameSpace(
    @JsonValue("id")
    val id: Int,
    @JsonValue("text")
    val text: String) : Parcelable