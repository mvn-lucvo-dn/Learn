package com.example.learnanything.customview.horizontalscroll

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class SlidingTabStrip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(
    context, attrs, defStyleAttr
) {

    init {
        setWillNotDraw(false)
    }
}