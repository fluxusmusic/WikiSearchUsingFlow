package com.mc.kim.test.dao.obj

import android.os.Parcelable
import com.mc.kim.remote.annotation.JsonValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class Titles(
    @JsonValue("canonical")
    val canonical: String,
    @JsonValue("normalized")
    val normalized: String,
    @JsonValue("display")
    val display: String) : Parcelable