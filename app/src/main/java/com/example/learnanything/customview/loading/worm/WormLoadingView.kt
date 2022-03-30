package com.example.learnanything.customview.loading.worm

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import androidx.core.content.ContextCompat
import com.example.learnanything.R
import com.example.learnanything.extension.dpToPx
import kotlin.math.max

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class WormLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 150F
        private const val DEFAULT_POINTS_AMOUNT = 5
        private const val DURATION = 1000L
        private const val DELAY_TIME = 120L
    }

    private var circlePaint: Paint
    private var defaultSize = DEFAULT_SIZE.dpToPx(context)
    private var radius = 0F
    private var startX = 0F
    private var moveScope: MutableList<Float>
    private var circleSize = 1F
    private var pointAmount = DEFAULT_POINTS_AMOUNT

    init {
        circlePaint = initPaint(R.color.white)
        moveScope = MutableList(pointAmount) { 1F }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(defaultSize, widthMeasureSpec)
        val height = resolveSize(defaultSize, heightMeasureSpec)
        /**
         * To make sure points always in center of View
         */
        radius = width.toFloat() / (pointAmount * 2) - (max(paddingStart, paddingEnd))
        circleSize = radius * 2
        startX = width / 2F - circleSize * pointAmount.toFloat() / 2
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawPoints(canvas)
    }

    /**
     * use a list to set value for each points
     * startDelay to make animation run sequential
     */
    internal fun startAnimation() {
        for (i in 0 until pointAmount) {
            val valueAnimator = ValueAnimator.ofFloat(1F, 0.5F, 1.5F, 1F)
            valueAnimator.apply {
                duration = DURATION
                repeatCount = Animation.INFINITE
                startDelay = i * DELAY_TIME
            }
            valueAnimator.addUpdateListener {
                (it?.animatedValue as? Float)?.let { value ->
                    moveScope[i] = value
                    invalidate()
                }
            }
            valueAnimator.start()
        }
    }

    /**
     * instead of calculating coordinates X each points, use translate to move coordinates of view then draw points
     */
    private fun drawPoints(canvas: Canvas?) {
        canvas?.apply {
            for (i in 0 until pointAmount) {
                save()
                translate(startX + circleSize * i, 0F)
                drawCircle(radius, (height / 2F) * moveScope[i], radius, circlePaint)
                restore()
            }
        }
    }

    private fun initPaint(
        colorRes: Int? = null,
        style: Paint.Style = Paint.Style.FILL
    ) = Paint(
        Paint.ANTI_ALIAS_FLAG and Paint.DITHER_FLAG
    ).apply {
        colorRes?.let {
            color = ContextCompat.getColor(context, it)
        }
        this.style = style
    }
}