package com.mc.kim.test.ui.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecoration(
    dividerColor: Int,
    val dividerHeight: Int,
    val leftPadding: Int,
    val rightPadding: Int
) :
    RecyclerView.ItemDecoration() {

    var dividerPaint: Paint

    init {
        dividerPaint = Paint()
        dividerPaint.style = Paint.Style.FILL
        dividerPaint.color = dividerColor
        dividerPaint.isAntiAlias = true
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        val width = parent.width
        for (childViewIndex in 0 until childCount) {
            val view = parent.getChildAt(childViewIndex)
            val itemTop = ViewCompat.getY(view).toInt() + view.height
            c.drawRect(Rect().apply {
                left = leftPadding
                top = itemTop
                right = width - rightPadding
                bottom = top + dividerHeight
            }, dividerPaint)
        }
    }


}