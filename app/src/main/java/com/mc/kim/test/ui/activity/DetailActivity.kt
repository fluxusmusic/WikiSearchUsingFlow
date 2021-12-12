package com.mc.kim.test.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.mc.kim.test.R
import com.mc.kim.test.toolkit.ACTION_START_WEB
import com.mc.kim.test.ui.fragment.WebViewFragment

class DetailActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (savedInstanceState == null) {
            when (intent.action) {
                ACTION_START_WEB -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, WebViewFragment().apply {
                            arguments = intent.extras
                        }).commit()
                }
            }
        }
    }
}