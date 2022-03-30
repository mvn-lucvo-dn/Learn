package com.example.learnanything.customview.loading.spinner

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.example.learnanything.R
import com.example.learnanything.extension.dpToPx
import kotlin.math.min

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class SpinnerLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context, attrs, defStyleAttr
) {

    companion object {
        private const val DURATION = 2000L
        private const val DELAY_DURATION = 100L
        private const val DEFAULT_TAIL_AMOUNT = 4
        private const val DEFAULT_SIZE = 150F
        private const val MAX_DEGREE = 360F
        private const val DEFAULT_POINT_AMOUNT = 2
    }

    private var desiredSize = DEFAULT_SIZE.dpToPx(context)
    private val pointPaint: Paint
    private var radius = 0F
    private var rotateScope: MutableList<Float>

    init {
        val size = DEFAULT_TAIL_AMOUNT + DEFAULT_POINT_AMOUNT / 2
        pointPaint = initPaint(R.color.white)
        rotateScope = MutableList(size) { 0F }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(desiredSize, widthMeasureSpec)
        val height = resolveSize(desiredSize, heightMeasureSpec)
        radius = width / 10F
        setMeasuredDimension(min(width, height), min(width, height))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCenterPoints(canvas)
        drawTailPoints(canvas)
    }

    internal fun startAnimation() {
        for (i in 0 until rotateScope.size) {
            val rotateAnimator = ValueAnimator.ofFloat(0F, MAX_DEGREE)
            rotateAnimator?.apply {
                duration = DURATION
                startDelay = i * DELAY_DURATION
                interpolator = AccelerateDecelerateInterpolator()

                addUpdateListener {
                    (it?.animatedValue as? Float)?.let { value ->
                        rotateScope[i] = value
                        invalidate()
                    }
                }
            }
            if (i == rotateScope.lastIndex - 1) {
                rotateAnimator.doOnEnd {
                    startAnimation()
                }
            }
            rotateAnimator.start()
        }
    }

    private fun drawTailPoints(canvas: Canvas?) {
        var smallRadius: Float
        val halfWidth = width / 2F
        val halfHeight = height / 2F
        canvas?.apply {
            for (j in 0 until DEFAULT_TAIL_AMOUNT) {
                save()
                rotate(rotateScope[j + 1], halfWidth, halfHeight)
                smallRadius = radius * (DEFAULT_TAIL_AMOUNT * 2 - j) / 10
                drawCircle(halfWidth, radius, smallRadius, pointPaint)
                drawCircle(halfWidth, height - radius, smallRadius, pointPaint)
                restore()
            }
        }
    }

    private fun drawCenterPoints(canvas: Canvas?) {
        val halfWidth = width / 2F
        val halfHeight = height / 2F
        canvas?.apply {
            save()
            rotate(rotateScope[0], halfWidth, halfHeight)
            drawCircle(halfWidth, radius, radius, pointPaint)
            drawCircle(halfHeight, height - radius, radius, pointPaint)
            restore()
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