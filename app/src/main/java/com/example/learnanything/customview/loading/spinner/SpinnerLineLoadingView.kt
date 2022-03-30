package com.example.learnanything.customview.loading.spinner

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat
import com.example.learnanything.R
import com.example.learnanything.extension.dpToPx
import kotlin.math.min

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class SpinnerLineLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context, attrs, defStyleAttr
) {

    companion object {
        private const val DELAY_DURATION = 100L
        private const val DEFAULT_SIZE = 150F
        private const val MAX_DEGREE = 360F
        private const val DEFAULT_POINT_AMOUNT = 10
        private const val MAX_ALPHA = 255
        private const val RADIUS = 4F
        private const val DEFAULT_PADDING = 3F
    }

    private var desiredSize = DEFAULT_SIZE.dpToPx(context)
    private var widthLine = 0F
    private var lineRect = RectF()
    private var rectanglePaint: Paint
    private var alphas = mutableListOf<Int>()
    private var padding = DEFAULT_PADDING.dpToPx(context)
    private var radius = RADIUS.dpToPx(context).toFloat()

    init {
        rectanglePaint = initPaint(R.color.white)
        alphas = MutableList(DEFAULT_POINT_AMOUNT) { MAX_ALPHA / 2 }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(desiredSize, widthMeasureSpec)
        val height = resolveSize(desiredSize, heightMeasureSpec)
        setMeasuredDimension(min(width, height), min(width, height))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthLine = w.div(DEFAULT_POINT_AMOUNT).toFloat()
        lineRect.apply {
            left = w / 2 - widthLine / 2
            right = left + widthLine
            top = padding.toFloat()
            bottom = widthLine * 2.5F
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val halfWidth = width / 2F
        val halfHeight = height / 2F
        /**
         * Note: default rotate with pivotX = 0 , pivotY = 0
         */
        canvas?.apply {
            for (i in 0 until DEFAULT_POINT_AMOUNT) {
                val degree = (MAX_DEGREE / DEFAULT_POINT_AMOUNT) * i
                save()
                rotate(degree, halfWidth, halfHeight)
                rectanglePaint.alpha = alphas[i]
                drawRoundRect(lineRect, radius, radius, rectanglePaint)
                restore()
            }
        }
    }

    internal fun startAnimation() {
        for (i in 0 until DEFAULT_POINT_AMOUNT) {
            val alphaAnimation = ValueAnimator.ofInt(MAX_ALPHA / 2, MAX_ALPHA).apply {
                duration = DELAY_DURATION * (alphas.size - 1)
                repeatCount = ValueAnimator.INFINITE
                startDelay = i * DELAY_DURATION
                interpolator = AccelerateInterpolator()
            }
            alphaAnimation.addUpdateListener {
                (it.animatedValue as? Int)?.let { value ->
                    alphas[i] = value
                    invalidate()
                }
            }
            alphaAnimation.start()
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