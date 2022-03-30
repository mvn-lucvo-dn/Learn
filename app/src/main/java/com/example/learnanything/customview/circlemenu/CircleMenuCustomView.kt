package com.example.learnanything.customview.circlemenu

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import com.example.learnanything.R
import com.example.learnanything.databinding.CustomCircleMenuBinding
import com.example.learnanything.extension.makeGone
import com.example.learnanything.extension.makeVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class CircleMenuCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_DEGREE = 360F
        private const val DEFAULT_DURATION = 500L
    }

    private var desiSize = 0F
    private var ringRadius = 0F
    private var isOpen = false
    private var items = mutableListOf<FloatingActionButton>()
    private var distance = 0F
    private val icons = mutableListOf(
        R.drawable.ic_home_white_24dp,
        R.drawable.ic_notifications_white_24dp,
        R.drawable.ic_place_white_24dp,
        R.drawable.ic_search_white_24dp,
        R.drawable.ic_settings_white_24dp
    )
    private val colors = mutableListOf(
        ContextCompat.getColor(context, R.color.holo_blue_light),
        ContextCompat.getColor(context, R.color.holo_green_dark),
        ContextCompat.getColor(context, R.color.holo_red_light),
        ContextCompat.getColor(context, R.color.holo_purple),
        ContextCompat.getColor(context, R.color.holo_orange_light)
    )
    private var binding: CustomCircleMenuBinding =
        CustomCircleMenuBinding.inflate(LayoutInflater.from(context), this)

    init {
        initItems()
        binding.circleCenterFAB.setOnClickListener {
            if (isOpen){
                handleCloseItems()
            } else {
                handleOpenItems()
            }
            isOpen = !isOpen
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculateSize()
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val width = desiSize.coerceAtMost(widthSize.toFloat())
        setMeasuredDimension(width.toInt(), width.toInt())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    private fun initPositionItems(value: Float, scaleValue: Float) {
        var degree = 0F
        val degreeStep = DEFAULT_DEGREE / items.size
        val center = width / 2
        for (fab in items) {
            //Radius = distance + 1/2 center fab + 1/2 center item
            val radius = value + fab.width
            val radian = Math.toRadians(degree.toDouble())
            //  - fab.width / 2 to make x and y are center of fab item
            val x = center + (radius * sin(radian)) - fab.width / 2
            val y = center - (radius * cos(radian)) - fab.width / 2
            fab.apply {
                this.x = x.toFloat()
                this.y = y.toFloat()
                scaleX = scaleValue
                scaleY = scaleValue
            }
            degree += degreeStep
        }
    }

    private fun calculateSize() {
        val widthFAB = binding.circleCenterFAB.measuredWidth
        distance = widthFAB.toFloat()
        desiSize = 5 * widthFAB.toFloat()
    }

    private fun initItems() {
        val itemCount = min(icons.size, colors.size)
        for (i in 0 until itemCount) {
            val fAb = FloatingActionButton(context).apply {
                setImageResource(icons[i])
                backgroundTintList = ColorStateList.valueOf(colors[i])
                // Remove Shadow FAB
                stateListAnimator = null
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                scaleX = 0F
                scaleY = 0F
            }
            addView(fAb)
            items.add(fAb)
        }
    }

    private fun handleCloseItems() {
        val animation = ValueAnimator.ofFloat(distance, 0F).apply {
            duration = DEFAULT_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            doOnEnd {
                for (item in items){
                    item.makeGone()
                }
            }
        }
        animation.addUpdateListener {
            (it?.animatedValue as? Float)?.let { value ->
                initPositionItems(value, 1 -it.animatedFraction)
            }
        }
        animation.start()
    }

    private fun handleOpenItems() {
        val animation = ValueAnimator.ofFloat(0F, distance).apply {
            duration = DEFAULT_DURATION
            interpolator = OvershootInterpolator()
            doOnStart {
                for (item in items){
                    item.makeVisible()
                }
            }
        }
        animation.addUpdateListener {
            (it?.animatedValue as? Float)?.let { value ->
                initPositionItems(value, it.animatedFraction)
            }
        }
        animation.start()
    }

    private fun Float.dpToPx() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    )
}