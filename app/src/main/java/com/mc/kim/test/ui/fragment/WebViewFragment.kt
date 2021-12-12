package com.mc.kim.test.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.mc.kim.remote.util.Log
import com.mc.kim.test.dao.response.WikiData
import com.mc.kim.test.databinding.FragmentWebviewContainerBinding
import com.mc.kim.test.toolkit.KEY_WIKI_DATA

class WebViewFragment : Fragment() {
    private val TAG: String = WebViewFragment::class.simpleName!!
    private lateinit var binding: FragmentWebviewContainerBinding


    private val backPressCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                requireActivity().finish()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebviewContainerBinding.inflate(inflater, container, false)
        var data = arguments?.get(KEY_WIKI_DATA) ?: null
        if (data != null && data is WikiData) {
            val wikiData = data
            binding.webView.apply {
                if (Log.INCLUDE) {
                    Log.d(TAG, "wikiData : ${wikiData}")
                }
                loadUrl(wikiData.getHtmlUrl())
            }
        }
        return binding.root
    }

    private fun WikiData.getHtmlUrl(): String =
        "https://en.wikipedia.org/api/rest_v1/page/html/${titles.canonical}"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(backPressCallback)
    }

    override fun onDetach() {
        super.onDetach()
        binding.webView.destroy()
    }
}