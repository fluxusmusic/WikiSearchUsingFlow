package com.mc.kim.test.toolkit

import android.view.View
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun View.clicks() = callbackFlow<Unit> {
    val listener = View.OnClickListener {
        trySend(Unit)
    }
    setOnClickListener(listener)
    awaitClose {
        setOnClickListener(null)
    }
}