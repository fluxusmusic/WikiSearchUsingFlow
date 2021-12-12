package com.mc.kim.test.toolkit

import android.app.Activity
import android.content.Intent
import com.mc.kim.test.dao.response.WikiData

val ACTION_MAIN = Intent.ACTION_MAIN
val ACTION_START_SEARCH = "mc.kim.intent.SEARCH_START"
val ACTION_START_WEB = "mc.kim.intent.WEB_START"
val KEY_WIKI_DATA = "key_wiki"

fun Activity.startWebActivity(wikiData: WikiData) {
    val intent: Intent = Intent(ACTION_START_WEB)
    intent.putExtra(KEY_WIKI_DATA, wikiData)
    startActivity(intent)
}

fun Activity.startSearchActivity(wikiData: WikiData) {
    val intent: Intent = Intent(ACTION_START_SEARCH)
    intent.putExtra(KEY_WIKI_DATA, wikiData)
    startActivity(intent)
}