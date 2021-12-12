package com.mc.kim.test.toolkit

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.mc.kim.remote.api.ResponseResult
import com.mc.kim.test.R
import com.mc.kim.test.ui.dialog.NoticeDialogFragment


val KEY_TITLE: String = "key_title"
val KEY_DESCRIPTION: String = "key_description"
fun ResponseResult.Error.makeResult(resources: Resources, fragmentManager: FragmentManager) {
    NoticeDialogFragment().apply {
        arguments = Bundle().apply {
            putString(KEY_TITLE, resources.getString(R.string.error_title))
            putString(KEY_DESCRIPTION, "Code:$errorCode\n${errorMessage}")
        }
    }.show(fragmentManager, NoticeDialogFragment.CLASS_NAME)
}


fun ResponseResult.Fail.makeResult(resources: Resources, fragmentManager: FragmentManager) {
    NoticeDialogFragment().apply {
        arguments = Bundle().apply {
            putString(KEY_TITLE, resources.getString(R.string.fail_title))
            putString(KEY_DESCRIPTION, "${exception.message}")
        }
    }.show(fragmentManager, NoticeDialogFragment.CLASS_NAME)
}
