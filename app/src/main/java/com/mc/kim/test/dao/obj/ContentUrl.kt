package com.mc.kim.test.dao.obj

import android.os.Parcelable
import com.mc.kim.remote.annotation.JsonValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentUrl(
    @JsonValue("desktop")
    val desktop: WikiUrl,
    @JsonValue("mobile")
    val mobile: WikiUrl
) : Parcelable