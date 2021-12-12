package com.mc.kim.test.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.mc.kim.test.R
import com.mc.kim.test.toolkit.ACTION_START_SEARCH
import com.mc.kim.test.toolkit.ACTION_START_WEB
import com.mc.kim.test.ui.fragment.SearchHomeFragment
import com.mc.kim.test.ui.fragment.WebViewFragment
import kotlinx.coroutines.launch

class SearchHomeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_home)
        if (savedInstanceState == null) {
            when (intent.action) {
                ACTION_START_SEARCH -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SearchHomeFragment().apply {
                            arguments = intent.extras
                        }).commit()
                }
                else ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SearchHomeFragment().apply {
                            arguments = intent.extras
                        }).commit()
                }
            }
        }
    }
}