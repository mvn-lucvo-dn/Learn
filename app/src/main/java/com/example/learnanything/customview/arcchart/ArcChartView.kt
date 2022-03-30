package com.example.learnanything.customview.arcchart

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnanything.R
import kotlin.math.round

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class ArcChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_STROKE_WIDTH = 20F
        private const val DEFAULT_VIEW_SIZE = 150
        private const val DEFAULT_SPACE_WIDTH = 24F
        private const val START_ANGLE = 270F
        private const val DEFAULT_ANIMATION_DURATION = 2000L
        private const val SWEEP_ANGLE = 360F
        private const val DEFAULT_SIZE_TEXT = 20F
    }

    private var strokeSize = DEFAULT_STROKE_WIDTH
    private var defaultPaint: Paint
    private var arcPaint: Paint
    private var textPaint: Paint
    private var rectArc = RectF()
    private var value = 50F
    private var total = 100F
    private var sweepAngle = 0F
    private var animationValue = 0F

    init {
        defaultPaint = initPaint(R.color.colorDefaultSaleArc)
        arcPaint = initPaint(R.color.teal_200)
        textPaint = initPaint(R.color.teal_200).apply {
            style = Paint.Style.FILL
            textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_SIZE_TEXT,
                context.resources.displayMetrics
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val width = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> DEFAULT_VIEW_SIZE.coerceAtMost(widthSize)
            MeasureSpec.UNSPECIFIED -> DEFAULT_VIEW_SIZE
            else -> DEFAULT_VIEW_SIZE
        }
        setMeasuredDimension(width, width)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rectArc.set(
            paddingLeft.toFloat() + DEFAULT_SPACE_WIDTH,
            paddingTop.toFloat() + DEFAULT_SPACE_WIDTH,
            width.toFloat() - paddingLeft.toFloat() - DEFAULT_SPACE_WIDTH,
            height.toFloat() - paddingTop.toFloat() - DEFAULT_SPACE_WIDTH
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawDefaultArc(canvas)
        drawProgressArc(canvas)
        drawText(canvas)
    }

    internal fun setData(value: Float, total: Float) {
        this.value = value
        this.total = total
        calculateData()
        animationChart()
    }

    @SuppressLint("Recycle")
    private fun animationChart() {
        val animator = ValueAnimator.ofFloat(0F, 1F)
        animator.apply {
            duration = DEFAULT_ANIMATION_DURATION
            addUpdateListener {
                (it?.animatedValue as? Float)?.let { value ->
                    animationValue = value
                    invalidate()
                }
            }
            start()
        }
    }

    private fun calculateData() {
        sweepAngle = (SWEEP_ANGLE * value) / total
    }

    private fun drawDefaultArc(canvas: Canvas?) {
        canvas?.drawArc(
            rectArc, START_ANGLE, SWEEP_ANGLE, false, defaultPaint
        )
    }

    private fun drawProgressArc(canvas: Canvas?) {
        canvas?.drawArc(
            rectArc, START_ANGLE, sweepAngle * animationValue, false, arcPaint
        )
    }

    private fun drawText(canvas: Canvas?) {
        val rect = Rect()
        val valueString = round(value * animationValue).toInt().toString()
        textPaint.getTextBounds(valueString, 0, valueString.length, rect)
        val haftHeight = rect.height() / 2
        val haftWidth = textPaint.measureText(valueString)/2
        canvas?.drawText(
            valueString,
            rectArc.centerX() - haftWidth,
            rectArc.centerY() - haftHeight,
            textPaint
        )
    }

    private fun initPaint(
        colorRes: Int? = null,
        cap: Paint.Cap = Paint.Cap.ROUND
    ): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            colorRes?.let {
                color = ContextCompat.getColor(context, colorRes)

            }
            //https://stackoverflow.com/questions/56791335/android-canvas-line-strokecap-round-larger-than-simple-line
            strokeCap = cap
            style = Paint.Style.STROKE
            strokeWidth = strokeSize
        }
}