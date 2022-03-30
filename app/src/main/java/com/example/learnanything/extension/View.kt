package com.example.learnanything.extension

import android.content.Context
import android.util.TypedValue
import android.view.View

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */

internal fun View.makeVisible() {
    visibility = View.VISIBLE
}

internal fun View.makeGone() {
    visibility = View.GONE
}

internal fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

internal fun Float.dpToPx(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    context.resources.displayMetrics
).toInt()