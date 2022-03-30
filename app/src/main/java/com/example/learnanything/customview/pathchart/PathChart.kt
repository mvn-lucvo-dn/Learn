package com.example.learnanything.customview.pathchart

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log.d
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnanything.R
import kotlin.math.ceil

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class PathChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context, attrs, defStyleAttr
) {

    companion object {
        private const val DEFAULT_PADDING = 20F
        private const val DEFAULT_STROKE_WIDTH = 5F
        private const val RADIUS = 10F
        private const val DEFAULT_SIZE_TEXT = 20F
    }

    private var startColor = ContextCompat.getColor(context, R.color.teal_200)
    private var endColor = ContextCompat.getColor(context, R.color.white)
    private var colors = intArrayOf(startColor, endColor)
    private var gradientPaint: Paint
    private var guideLinePaint: Paint
    private var markerPaint: Paint
    private var chartPaint: Paint
    private var textPaint: Paint
    private var dataList = mutableListOf<Int>(10, 30, 20, 60, 50, 70, 112, 40)
    private var markers = mutableListOf<Markers>()
    private var zeroY = 0F
    private var pathChart = Path()
    private var padding = DEFAULT_PADDING
    private var widthValue = 0F
    private var heightPerUnit = 0F
    private var length = 0F
    private var isAnimationEnd = false

    init {
        gradientPaint = initPaint(paintStyle = Paint.Style.FILL)
        guideLinePaint = initPaint(R.color.colorGray).apply {
            /**
             * 10F : DashWidth
             * 20F: DashGap
             * https://titanwolf.org/Network/Articles/Article?AID=a83df051-cdce-4bfe-8706-00f74194b31b
             */
            pathEffect = DashPathEffect(floatArrayOf(20F, 10F), 0F)
        }
        markerPaint = initPaint(
            R.color.colorMarker,
            widthStroke = DEFAULT_STROKE_WIDTH * 2,
            paintStyle = Paint.Style.FILL_AND_STROKE
        )
        chartPaint = initPaint(R.color.colorMarker, widthStroke = DEFAULT_STROKE_WIDTH * 2)
        textPaint = initPaint(R.color.colorGray, Paint.Style.FILL).apply {
            textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_SIZE_TEXT,
                context.resources.displayMetrics
            )
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        calculatorPosition()
        gradientPaint.apply {
            shader = getChartGradient()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawValue(canvas)
        drawGradient(canvas)
        drawGuildLine(canvas)
        drawLineAndMarker(canvas)
    }

    private fun drawValue(canvas: Canvas?) {
        val xPosition = markers.last().xPosition + padding
        val max = dataList.maxByOrNull { it } ?: 0

        val rect = Rect()
        val maxValue = if (max >= 100) {
            (ceil((max) / 100.toDouble()) * 100).toInt()
        } else {
            (max / 10) * 10
        }
        for (i in 0..maxValue / 10) {
            val value = 10 * i
            textPaint.getTextBounds(value.toString(), 0, value.toString().length, rect)
            canvas?.drawText(
                value.toString(),
                xPosition,
                zeroY - (value * heightPerUnit) - rect.centerY(),
                textPaint
            )
        }
    }

    private fun drawLineAndMarker(canvas: Canvas?) {
        /**
         * draw first init
         */
        pathChart.reset()
        if (isAnimationEnd){
            canvas?.drawCircle(padding + RADIUS / 2, zeroY, RADIUS, markerPaint)
        }
        pathChart.moveTo(padding + RADIUS / 2, zeroY)
        var i = 0
        do {
            val marker = markers[i]
            if (isAnimationEnd){
                canvas?.drawCircle(
                    marker.xPosition,
                    marker.yPosition,
                    RADIUS,
                    markerPaint
                )
            }
            pathChart.lineTo(
                marker.xPosition, marker.yPosition
            )
            i++
        } while (i < markers.size)
        canvas?.drawPath(pathChart, chartPaint)

        val measure = PathMeasure(pathChart, false)
        length = measure.length
    }

    private fun setPhase(phase: Float) {
        chartPaint.pathEffect = createPathEffect(length, phase, 0.0f)
        invalidate() //will call onDraw
    }

    private fun createPathEffect(pathLength: Float, phase: Float, offset: Float): PathEffect? {
        return DashPathEffect(
            floatArrayOf(pathLength, pathLength),
            (phase * pathLength).coerceAtLeast(offset)
        )
    }

    private fun drawGuildLine(canvas: Canvas?) {
        for (i in 0 until markers.size step 2) {
            val marker = markers[i]
            canvas?.drawLine(
                marker.xPosition, padding, marker.xPosition, zeroY, guideLinePaint
            )
        }
    }

    internal fun setAnimation() {
        val animator = ObjectAnimator.ofFloat(this, "phase", 1.0f, 0.0f)
        animator.duration = 3000
        animator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {
                //No-op
            }

            override fun onAnimationEnd(animation: Animator?) {
                isAnimationEnd = true
                invalidate()
            }

            override fun onAnimationCancel(animation: Animator?) {
                //No-op
            }

            override fun onAnimationRepeat(animation: Animator?) {
                //No-op
            }

        })
        animator.start()
    }

    internal fun setData(dataList: MutableList<Int>) {
        this.dataList = dataList
    }

    private fun calculatorPosition() {
        val max = dataList.maxByOrNull { it } ?: 0
        widthValue = textPaint.measureText(max.toString())
        val chartHeight = height - 2 * padding
        val widthPerUnit = (width - 2 * padding - widthValue) / (dataList.size)
        heightPerUnit = chartHeight / (max - 0)
        zeroY = chartHeight + padding
        for (i in 0 until dataList.size) {
            val value = dataList[i]
            val y = zeroY - heightPerUnit * value
            val x = padding + widthPerUnit * (i + 1)
            markers.add(Markers(value, x, y))
        }
    }

    private fun drawGradient(canvas: Canvas?) {
        pathChart.reset()
        pathChart.moveTo(padding, zeroY)
        for (marker in markers) {
            pathChart.lineTo(marker.xPosition, marker.yPosition)
        }
        pathChart.lineTo(markers.last().xPosition, zeroY)
        pathChart.lineTo(padding, zeroY)
        canvas?.drawPath(pathChart, gradientPaint)
    }

    private fun getChartGradient(): Shader {
        return LinearGradient(
            0F,
            DEFAULT_PADDING,
            0F,
            zeroY,
            colors,
            null,
            Shader.TileMode.CLAMP
        )
    }

    private fun initPaint(
        colorRes: Int? = null,
        paintStyle: Paint.Style = Paint.Style.STROKE,
        widthStroke: Float? = null
    ): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            colorRes?.let {
                color = ContextCompat.getColor(context, colorRes)
            }
            widthStroke?.let {
                strokeWidth = it
            }
            style = paintStyle
        }
}