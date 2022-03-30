package com.example.learnanything.customview.piechart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log.d
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.plus
import com.example.learnanything.R
import java.lang.Math.atan2
import kotlin.random.Random

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class CustomPieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context, attrs, defStyleAttr
) {
    companion object {
        private const val DEFAULT_PADDING = 60F
        private const val DEFAULT_VIEW_SIZE = 250
        private var DEFAULT_ANGLE = 360F
    }

    private var costInfoList = mutableListOf<CostInfo>()
    private var sum = 1
    private var arcPaint: Paint
    private var rectPie = RectF()
    private var startAngle = 270F
    private var sweepAngle = 0F
    private var anglePerUnit = 0F
    private var colors = mutableListOf<IntArray>()
    private var indexSelect = -1

    init {
        arcPaint = initPaint()
        arcPaint.color = ContextCompat.getColor(context, R.color.black)
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
        calculateChart()
        for (i in 0 until costInfoList.size) {
            colors.add(getRandomColors())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawPieChart(canvas)
    }

    private fun calculateChart() {
        val left = DEFAULT_PADDING
        val right = width - DEFAULT_PADDING
        val top = DEFAULT_PADDING
        val bottom = height - DEFAULT_PADDING
        rectPie.set(left, top, right, bottom)

        anglePerUnit = DEFAULT_ANGLE / sum
    }

    private fun drawPieChart(canvas: Canvas?) {
        for ((index, costInfo) in costInfoList.withIndex()) {
            val cost = costInfo.cost
            val color = colors[index]
            arcPaint.setARGB(
                color[0], color[1], color[2], color[3],
            )
            sweepAngle = cost * anglePerUnit
            costInfo.isActive = indexSelect == index
            if (costInfo.isActive){
                rectPie.apply {
                    left = 0F
                    right = width.toFloat()
                    top = 0F
                    bottom = height.toFloat()
                }
            } else {
                rectPie.apply {
                    left = DEFAULT_PADDING
                    right = width - DEFAULT_PADDING
                    top = DEFAULT_PADDING
                    bottom = height - DEFAULT_PADDING
                }
            }
            canvas?.drawArc(
                rectPie, startAngle, sweepAngle, true, arcPaint
            )
            startAngle += sweepAngle
        }
    }

    internal fun setData(costInfoList: MutableList<CostInfo>) {
        this.costInfoList = costInfoList
        sum = costInfoList.sumOf {
            it.cost
        }
    }

    private fun initPaint(
        paintStyle: Paint.Style = Paint.Style.FILL
    ): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            style = paintStyle
        }

    private fun getRandomColors(): IntArray {
        val min = 0
        val max = 255
        val transparency = 255 // from 0 to 255 ,255 means no transparency
        val red = Random.nextInt(max - min + 1) + min
        val green = Random.nextInt(max - min + 1) + min
        val blue = Random.nextInt(max - min + 1) + min
        return intArrayOf(transparency, red, green, blue)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
               val angleClicked = convertTouchEventPointToAngle(
                   width.toFloat(), height.toFloat(),
                   event.x, event.y
               )
                var startAngel = 0F
                var sweepAngle: Float
                for ((i, costInfo) in costInfoList.withIndex()){
                    sweepAngle = costInfo.cost * anglePerUnit
                    if ((startAngel+ sweepAngle) >= angleClicked){
                        indexSelect = i
                        break
                    } else {
                        startAngel += sweepAngle
                    }
                }
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun convertTouchEventPointToAngle(
        width: Float,
        height: Float,
        xPos: Float,
        yPos: Float
    ): Double {
        val x = xPos - (width * 0.5f)
        val y = yPos - (height * 0.5f)

        var angle = Math.toDegrees(kotlin.math.atan2(y.toDouble(), x.toDouble()) + Math.PI / 2)
        angle = if (angle < 0) angle + 360 else angle
        return angle
    }
}