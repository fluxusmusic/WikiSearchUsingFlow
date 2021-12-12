package com.mc.kim.test.ui.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import com.mc.kim.remote.api.ResponseResult
import com.mc.kim.test.dao.WikiDataManager
import com.mc.kim.test.dao.obj.Image
import com.mc.kim.test.dao.response.WikiData
import com.mc.kim.test.dao.response.WikiDataList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.util.*

private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
private val cacheSize = maxMemory / 8

class WikiRepository private constructor(
    private val wikiDataManager: WikiDataManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val lruCache: LruCache<String, Bitmap> = LruCache(cacheSize)
) {
    private val TAG:String = WikiRepository::class.simpleName!!
    val keywordStack: Stack<String> = Stack()

    @Throws(MalformedURLException::class)
    suspend fun loadImage(image: Image): Bitmap = withContext(ioDispatcher) {
        return@withContext lruCache.get(image.source)
            ?: BitmapFactory.decodeStream(URL(image.source).content as InputStream).also {
                lruCache.put(image.source, it)
            }
    }


    suspend fun searchSummary(keyword: String): ResponseResult<WikiData> =
        withContext(ioDispatcher) {
            keywordStack.push(keyword)
            wikiDataManager.requestSummary(keyword)
        }

    suspend fun loadRelatedList(wikiData: WikiData): ResponseResult<WikiDataList> =
        withContext(ioDispatcher) {
            wikiDataManager.requestRelatedLit(wikiData)
        }

    companion object {
        @Volatile
        private var instance: WikiRepository? = null

        fun getInstance(wikiDataManager: WikiDataManager) =
            instance ?: synchronized(this) {
                instance ?: WikiRepository(wikiDataManager).also { instance = it }
            }
    }
}