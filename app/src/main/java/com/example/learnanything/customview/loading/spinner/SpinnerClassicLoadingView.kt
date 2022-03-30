package com.example.learnanything.customview.loading.spinner

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnanything.R
import com.example.learnanything.extension.dpToPx
import kotlin.math.min

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class SpinnerClassicLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    companion object {
        private const val DURATION = 1000L
        private const val DELAY_DURATION = 100L
        private const val DEFAULT_SIZE = 150F
        private const val MAX_DEGREE = 360F
        private const val DEFAULT_POINT_AMOUNT = 10
        private const val MAX_ALPHA = 255
        private const val DEFAULT_PADDING = 3F
    }

    private var desiredSize = DEFAULT_SIZE.dpToPx(context)
    private var defaultPadding = DEFAULT_PADDING.dpToPx(context)
    private var radius = 0F
    private var pointPaint: Paint
    private var alphaPaint: MutableList<Int>
    private var spaceDegree = MAX_DEGREE / DEFAULT_POINT_AMOUNT

    init {
        pointPaint = initPaint(R.color.white)
        alphaPaint = MutableList(DEFAULT_POINT_AMOUNT) {
            MAX_ALPHA/2
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(desiredSize, widthMeasureSpec)
        val height = resolveSize(desiredSize, heightMeasureSpec)
        radius = width.toFloat() / DEFAULT_POINT_AMOUNT
        setMeasuredDimension(min(width, height), min(width, height))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val halfWidth = width / 2F
        val halfHeight = height / 2F
        canvas?.apply {
            for (i in 0 until DEFAULT_POINT_AMOUNT) {
                save()
                rotate(spaceDegree * i, halfWidth, halfHeight)
                pointPaint.alpha = alphaPaint[i]
                drawCircle(width / 2F, radius + defaultPadding, radius, pointPaint)
                restore()
            }
        }
    }

    @SuppressLint("Recycle")
    internal fun startAnimation() {
        for (i in 0 until DEFAULT_POINT_AMOUNT) {
            val alphaAnimator = ValueAnimator.ofInt(MAX_ALPHA / 2, MAX_ALPHA, MAX_ALPHA / 2)
            alphaAnimator.apply {
                duration = DURATION
                startDelay = i * DELAY_DURATION
                repeatCount = ValueAnimator.INFINITE

                addUpdateListener {
                    (it?.animatedValue as? Int)?.let { value ->
                        alphaPaint[i] = value
                        invalidate()
                    }
                }
            }
            alphaAnimator.start()
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
        alpha = (MAX_ALPHA / 2)
        this.style = style
    }
}