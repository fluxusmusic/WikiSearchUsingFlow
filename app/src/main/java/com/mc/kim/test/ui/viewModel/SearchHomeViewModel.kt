package com.mc.kim.test.ui.viewModel

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mc.kim.remote.api.ResponseResult
import com.mc.kim.remote.util.Log
import com.mc.kim.test.R
import com.mc.kim.test.dao.obj.Image
import com.mc.kim.test.dao.response.WikiData
import com.mc.kim.test.dao.response.WikiDataList
import com.mc.kim.test.ui.fragment.Intent.WikiIntent
import com.mc.kim.test.ui.fragment.viewstate.WikiState
import com.mc.kim.test.ui.repository.WikiRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.MalformedURLException

class SearchHomeViewModel internal constructor(
    private val wikiRepository: WikiRepository
) : ViewModel() {
    private val TAG = SearchHomeViewModel::class.simpleName!!
    val wikiIntent = Channel<WikiIntent>(Channel.UNLIMITED)
    private val _data = MutableStateFlow<WikiState>(WikiState.Idle)
    val data: StateFlow<WikiState> get() = _data
    private var currentKeyword:String = ""


    fun getCurrentKeyword(): String {
        return currentKeyword
    }

    init {
        handleIntent()
    }

    private fun handleIntent() = viewModelScope.launch{
        wikiIntent.consumeAsFlow().collect {intent->
            when (intent) {
                is WikiIntent.FetchData->{
                    getSearchResult(intent.keyword)
                }

                is WikiIntent.FetchDataList->{
                    getRelatedList(intent.wikiData)
                }
            }
        }
    }

    private fun getSearchResult(keyword: String) = viewModelScope.launch {
        currentKeyword = keyword
        _data.emit(WikiState.Loading)

        val result = wikiRepository.searchSummary(keyword)
        when(result){
            is ResponseResult.Success->{
                _data.emit(WikiState.ResultData(result))
            }
            is ResponseResult.Error->{
                _data.emit(WikiState.Error(result))
            }

            is ResponseResult.Fail->{
                _data.emit(WikiState.Fail(result))
            }
        }
    }

   private fun getRelatedList(wikiData: WikiData) = viewModelScope.launch {
       _data.emit(WikiState.Loading)
       val result = wikiRepository.loadRelatedList(wikiData)
       when(result){
           is ResponseResult.Success->{
               _data.emit(WikiState.ResultDataList(result))
           }
           is ResponseResult.Error->{
               _data.emit(WikiState.Error(result))
           }

           is ResponseResult.Fail->{
               _data.emit(WikiState.Fail(result))
           }
       }
    }



    fun getCachedImage(resources: Resources, image: Image, result: (Bitmap?) -> Unit) {
        viewModelScope.launch {
            try {
                result(wikiRepository.loadImage(image))
            } catch (e: MalformedURLException) {
                result(null)
            }
        }
    }
}