package com.mc.kim.test.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mc.kim.test.databinding.ViewSwipeSupportedRecyclerviewBinding

class SwipeSupportedRecyclerView : SwipeRefreshLayout, SwipeRefreshLayout.OnRefreshListener {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val binding: ViewSwipeSupportedRecyclerviewBinding =
        ViewSwipeSupportedRecyclerviewBinding.inflate(
            LayoutInflater.from(context), this, true
        )

    var adapter: RecyclerView.Adapter<*>?
        get() {
            return binding.list.adapter
        }
        set(adapter) {
            binding.list.adapter = adapter
        }

    var _onRefreshListener:OnRefreshListener? = null

    fun addItemDecoration(itemDecoration: RecyclerView.ItemDecoration){
        binding.list.addItemDecoration(itemDecoration)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setOnRefreshListener(this)

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setOnRefreshListener(null)
    }


    override fun onRefresh() {
        _onRefreshListener?.onRefresh()
        isRefreshing = false
    }
}