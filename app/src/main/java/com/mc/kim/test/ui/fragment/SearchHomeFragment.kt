package com.mc.kim.test.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.mc.kim.test.ui.Injector
import com.mc.kim.test.ui.repository.WikiRepository
import com.mc.kim.test.ui.viewModel.SearchHomeViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mc.kim.remote.api.ResponseResult
import com.mc.kim.remote.util.Log
import com.mc.kim.test.R
import com.mc.kim.test.dao.response.WikiData
import com.mc.kim.test.dao.response.WikiDataList
import com.mc.kim.test.databinding.FragmentSearchHomeBinding
import com.mc.kim.test.toolkit.KEY_WIKI_DATA
import com.mc.kim.test.toolkit.makeResult
import com.mc.kim.test.toolkit.startSearchActivity
import com.mc.kim.test.toolkit.startWebActivity
import com.mc.kim.test.ui.fragment.Intent.WikiIntent
import com.mc.kim.test.ui.fragment.adapter.SearchResultAdapter
import com.mc.kim.test.ui.fragment.loader.ResourceLoader
import com.mc.kim.test.ui.fragment.row.WikiRow
import com.mc.kim.test.ui.fragment.viewstate.WikiState
import com.mc.kim.test.ui.view.DividerItemDecoration
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchHomeFragment : Fragment() {
    private val TAG: String = SearchHomeFragment::class.simpleName!!
    private val viewModel: SearchHomeViewModel by viewModels {
        Injector.provideSearchHomeViewModelFactory(requireContext())
    }
    private lateinit var resourceLoader: ResourceLoader

    private val _onItemClickListener = object : WikiRow.OnItemClickListener {
        override fun onItemClick(wikiData: WikiData, type: WikiRow.RowType) {
            when (type) {
                WikiRow.RowType.Header -> {
                    requireActivity().startWebActivity(wikiData)
                }
                WikiRow.RowType.Item -> {
                    requireActivity().startSearchActivity(wikiData)
                }
            }

        }
    }

    private val _onKeywordSearchListener =
        TextView.OnEditorActionListener { textView, actionId, p2 ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (textView.text.isNotEmpty()) {
                        textView.clearFocus()
                        requestSearch(textView.text.toString())
                    }
                }
            }
            true
        }

    private val backPressCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            requireActivity().finish()
        }
    }

    private lateinit var binding: FragmentSearchHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchHomeBinding.inflate(inflater, container, false)
        with(viewModel) {
            resourceLoader = ResourceLoader(this, resources)
            val adapter = SearchResultAdapter()
            binding.keywordInputView.setOnEditorActionListener(_onKeywordSearchListener)
            binding.searchList._onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
                requestSearch(viewModel.getCurrentKeyword())
            }
            binding.searchList.addItemDecoration(
                DividerItemDecoration(
                    resources.getColor(R.color.color_91CEDC),
                    resources.getDimensionPixelOffset(R.dimen.divider_height),
                    0,
                    0
                )
            )
            binding.searchList.adapter = adapter
            subscribeData(adapter)
            var data = arguments?.get(KEY_WIKI_DATA) ?: null
            if (data != null && data is WikiData) {
                val wikiData = data
                requestSearch(wikiData.titles.canonical)
            }
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(backPressCallback)
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.viewModelScope.cancel()
    }

    private fun subscribeData(adapter: SearchResultAdapter) {
        viewModel.viewModelScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collect { state ->
                    if (Log.INCLUDE) {
                        Log.d(TAG, "state : ${state}")
                    }
                    when (state) {
                        is WikiState.Loading -> {

                        }
                        is WikiState.ResultData -> {
                            makeResult(adapter,state.result)
                        }
                        is WikiState.ResultDataList -> {
                            makeRelateResult(adapter,state.result)
                        }
                    }

                }
            }
        }
    }

    private fun makeResult(adapter: SearchResultAdapter, result: ResponseResult<WikiData>) {
        when (result) {
            is ResponseResult.Success -> {
                adapter.reset()
                adapter.addItem(
                    WikiRow.HeaderWikiRow(result.data, resourceLoader).apply {
                        onItemClickListener = _onItemClickListener
                    })
                loadRelatedContents(result.data)
            }

            is ResponseResult.Error -> {
                result.makeResult(resources, childFragmentManager)
            }

            is ResponseResult.Fail -> {
                result.makeResult(resources, childFragmentManager)
            }
        }
    }

    private fun makeRelateResult(
        adapter: SearchResultAdapter,
        result: ResponseResult<WikiDataList>
    ) {
        when (result) {
            is ResponseResult.Success -> {
                val wikiDataList = result.data.pages
                for (wikiData in wikiDataList) {
                    adapter.addItem(WikiRow.RelatedWikiRow(wikiData, resourceLoader).apply {
                        onItemClickListener = _onItemClickListener
                    })
                }
            }

            is ResponseResult.Error -> {
                result.makeResult(resources, childFragmentManager)
            }

            is ResponseResult.Fail -> {
                result.makeResult(resources, childFragmentManager)
            }

        }
    }



    private fun requestSearch(keyword: String) {
        binding.keywordInputView.text =
            Editable.Factory.getInstance().newEditable(keyword)
        with(viewModel) {
            viewModelScope.launch {
                viewModel.wikiIntent.send(WikiIntent.FetchData(keyword))
            }
        }
    }

    private fun loadRelatedContents(wikiData: WikiData) {
        with(viewModel) {
            viewModelScope.launch {
                viewModel.wikiIntent.send(WikiIntent.FetchDataList(wikiData))
            }
        }
    }
}

class SearchHomeViewModelFactory(
    private val repository: WikiRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) = SearchHomeViewModel(repository) as T
}