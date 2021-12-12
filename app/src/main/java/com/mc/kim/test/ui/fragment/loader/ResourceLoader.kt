package com.mc.kim.test.ui.fragment.loader

import android.content.res.Resources
import android.graphics.Bitmap
import com.mc.kim.test.dao.obj.Image
import com.mc.kim.test.ui.viewModel.SearchHomeViewModel

class ResourceLoader(private val viewModel: SearchHomeViewModel, private val resource: Resources) {
    fun loadResource(image: Image, callback: ResourceCallback) {

        viewModel.getCachedImage(resource, image) {
            callback.onLoaded(it)
        }
    }

    interface ResourceCallback {
        fun onLoaded(bitmap: Bitmap?)
    }
}
