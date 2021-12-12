package com.mc.kim.test.dao.response

import android.os.Parcelable
import com.mc.kim.remote.annotation.JsonValue
import com.mc.kim.test.dao.obj.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class WikiData(
    @JsonValue("type")
    val type: String,
    @JsonValue("title")
    val title: String,
    @JsonValue("displaytitle")
    val displayTitle: String,
    @JsonValue("namespace")
    val namespace: NameSpace,
    @JsonValue("wikibase_item")
    val wikibaseItem: String,
    @JsonValue("titles")
    val titles: Titles,
    @JsonValue("pageid")
    val pageId: Int,
    @JsonValue("thumbnail")
    val thumbnail: Image?,
    @JsonValue("originalimage")
    val originalImage: Image?,
    @JsonValue("lang")
    val lang: String,
    @JsonValue("dir")
    val dir: String,
    @JsonValue("revision")
    val revision: String,
    @JsonValue("tid")
    val tid: String,
    @JsonValue("timestamp")
    val timestamp: String,
    @JsonValue("description")
    val description: String?,
    @JsonValue("description_source")
    val descriptionSource: String?,
    @JsonValue("content_urls")
    val wikiUrl: ContentUrl,
    @JsonValue("extract")
    val extract: String,
    @JsonValue("extract_html")
    val extractHtml: String
) : Parcelable
