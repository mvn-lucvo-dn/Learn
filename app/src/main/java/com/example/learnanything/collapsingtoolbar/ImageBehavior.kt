package com.example.learnanything.collapsingtoolbar

import android.content.Context
import android.util.AttributeSet
import android.util.Log.d
import android.util.TypedValue
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class ImageBehavior @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<View>(context, attrs) {

    private var isInit = false
    private var startToolBarHeight = 0
    private var finalToolbarHeight = 0
    private var startHeight = 0
    private var finalHeight = 0
    private var startX = 0F
    private var finalX = 0F
    private var starY = 0F
    private var finalY = 0F


    init {
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            finalToolbarHeight = TypedValue.complexToDimensionPixelSize(
                tv.data,
                context.resources.displayMetrics
            )
            finalHeight = finalToolbarHeight - 40
        }
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (dependency is AppBarLayout) {
            init(parent, child, dependency)
            var currentToolbarHeight = startToolBarHeight + dependency.y
            if (currentToolbarHeight < finalToolbarHeight) {
                currentToolbarHeight = finalToolbarHeight.toFloat()
            }
            val amountMove = startToolBarHeight - currentToolbarHeight
            val totalMove = startToolBarHeight - finalToolbarHeight
            val progress = amountMove / totalMove
            val height = startHeight - progress * (startHeight - finalHeight)
            // change size view
            val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.width = height.toInt()
            layoutParams.height = height.toInt()
            child.layoutParams = layoutParams
            // change coordinates view
            val newX = startX - progress * (startX - finalX)
            val newY = starY - progress * (starY - finalY)
            child.x = newX
            child.y = newY
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }

    private fun init(parent: CoordinatorLayout, child: View, dependency: AppBarLayout) {
        if (!isInit) {
            startHeight = child.height
            startToolBarHeight = dependency.height
            isInit = true
            finalX = dependency.width.toFloat() - finalHeight
            finalY = 20F
            startX = child.x
            starY = child.y
        }
    }
}
