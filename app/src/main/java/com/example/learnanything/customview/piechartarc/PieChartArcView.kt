package com.example.learnanything.customview.piechartarc

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.learnanything.R
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.random.Random

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class PieChartArcView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_PADDING = 60F
        private const val DEFAULT_SMALL_CIRCLE = 120F
        private const val DEFAULT_SPACE = 1F
        private const val DEFAULT_VIEW_SIZE = 250
        private const val DEFAULT_ANGLE = 360F
        private const val START_ANGLE = 270F
        private const val ROTATE_DEGREE = 45F
    }

    private var piePaint: Paint
    private var textPaint: Paint
    private var smallCircle = DEFAULT_SMALL_CIRCLE
    private var startAngle = START_ANGLE
    private var total = 1F
    private var anglePerUnit = 0F
    private var rotate = ROTATE_DEGREE
    private var piePath = Path()
    private var innerRect = RectF()
    private var outerRect = RectF()
    private var colors = mutableListOf<IntArray>()
    private var analysisInfoList = mutableListOf<AnalysisInfo>()

    init {
        piePaint = initPaint()
        textPaint = initPaint(Paint.Style.STROKE).apply {
            textAlign = Paint.Align.CENTER
            textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20F,
                context.resources.displayMetrics
            )
            color = ContextCompat.getColor(context, R.color.black)
        }
        setLayerType(LAYER_TYPE_SOFTWARE, piePaint)
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateChart()
    }

    private fun calculateChart() {
        outerRect.set(
            DEFAULT_PADDING,
            DEFAULT_PADDING,
            width - DEFAULT_PADDING,
            height - DEFAULT_PADDING
        )
        setSizeInnerCircle()
        anglePerUnit = (DEFAULT_ANGLE - analysisInfoList.size * DEFAULT_SPACE) / total
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawSlice(canvas)
        drawDecorativeRing(canvas)
        drawPercentValues(canvas)
        drawIcon(canvas)
    }

    private fun drawIcon(canvas: Canvas?) {
        smallCircle = outerRect.centerX() * 0.5F
        setSizeInnerCircle()
        val matrix = Matrix()
        for (analysisInfo in analysisInfoList) {
            val sweepAngle = analysisInfo.value * anglePerUnit
            val x =
                getXPosition(
                    innerRect.centerX(), innerRect.width() / 2,
                    (startAngle + sweepAngle * 0.5)
                )
            val y =
                getYPosition(
                    innerRect.centerY(), innerRect.height() / 2,
                    (startAngle + sweepAngle * 0.5)
                )

            val xRotate =
                getXPosition(
                    innerRect.centerX(), innerRect.width() / 2,
                    (startAngle + rotate + sweepAngle * 0.5)
                )
            val yRotate =
                getYPosition(
                    innerRect.centerY(), innerRect.height() / 2,
                    (startAngle + rotate + sweepAngle * 0.5)
                )
            val bitmap = ContextCompat.getDrawable(context, analysisInfo.icon)?.toBitmap()
            bitmap?.let {
                matrix.postTranslate(x - it.width / 2, y - it.height / 2)
                matrix.postRotate(rotate, innerRect.centerX(), innerRect.centerY())
                matrix.postTranslate(-xRotate + x, -yRotate + y)
                canvas?.drawBitmap(it, matrix, null)
                matrix.reset()
            }
            startAngle += sweepAngle
        }
    }

    private fun drawPercentValues(canvas: Canvas?) {
        for (analysisInfo in analysisInfoList) {
            piePath.reset()
            val sweepAngle = analysisInfo.value * anglePerUnit
            val percent = analysisInfo.value * 100 / total
            val percentString = if (percent % 1.0 == 0.0) {
                percent.toInt().toString()
            } else {
                String.format("%.2f", percent)
            }
            /**
             * 0.15F means 0.3* outRect/2
             */
            piePath.addArc(innerRect, startAngle, sweepAngle)
            canvas?.drawTextOnPath(
                "$percentString%",
                piePath,
                0F,
                (-0.15F * outerRect.width()) / 4,
                textPaint
            )
            startAngle += sweepAngle
        }
    }

    private fun drawDecorativeRing(canvas: Canvas?) {
        smallCircle = outerRect.centerX() * 0.7F
        piePaint.color = ContextCompat.getColor(context, R.color.colorLightGray)
        setSizeInnerCircle()
        setPathSolidArc(DEFAULT_ANGLE)
        canvas?.drawPath(piePath, piePaint)
    }

    private fun drawSlice(canvas: Canvas?) {
        for ((i, analysisInfo) in analysisInfoList.withIndex()) {
            val colorArray = colors[i]
            piePaint.setARGB(
                colorArray[0], colorArray[1], colorArray[2], colorArray[3]
            )
            val sweepAngle = round(analysisInfo.value * anglePerUnit)
            setPathSolidArc(sweepAngle)
            canvas?.drawPath(piePath, piePaint)
            startAngle += sweepAngle + DEFAULT_SPACE
        }
    }

    private fun setPathSolidArc(sweepAngle: Float) {
        /**
         * centerX = (left+ right)/2
         * width = (right - left)
         */
        piePath.reset()
        val innerX =
            getXPosition(innerRect.centerX(), innerRect.width() / 2, startAngle.toDouble())
        val innerY =
            getYPosition(innerRect.centerY(), innerRect.height() / 2, startAngle.toDouble())
        val outerX =
            getXPosition(
                outerRect.centerX(),
                outerRect.width() / 2,
                (startAngle + sweepAngle).toDouble()
            )
        val outerY =
            getYPosition(
                outerRect.centerY(),
                outerRect.height() / 2,
                (startAngle + sweepAngle).toDouble()
            )
        piePath.moveTo(innerX, innerY)
        piePath.addArc(
            innerRect, startAngle, sweepAngle
        )
        piePath.lineTo(outerX, outerY)
        piePath.addArc(outerRect, startAngle + sweepAngle, -sweepAngle)
        piePath.lineTo(innerX, innerY)
    }

    internal fun setData(analysisInfoList: MutableList<AnalysisInfo>, total: Float) {
        this.analysisInfoList = analysisInfoList
        this.total = total
        for (i in 0 until analysisInfoList.size) {
            colors.add(getRandomColors())
        }
    }

    private fun setSizeInnerCircle() {
        innerRect.set(
            outerRect.centerX() - smallCircle,
            outerRect.centerY() - smallCircle,
            outerRect.centerX() + smallCircle,
            outerRect.centerY() + smallCircle
        )
        startAngle = START_ANGLE
    }

    private fun getXPosition(
        centerX: Float, radius: Float, degree: Double
    ): Float {
        val radian = Math.toRadians(degree + 90)
        return (centerX + radius * sin(radian)).toFloat()
    }

    private fun getYPosition(
        centerY: Float, radius: Float, degree: Double
    ): Float {
        val radian = Math.toRadians(degree + 90)
        return (centerY - radius * cos(radian)).toFloat()
    }

    private fun initPaint(
        paintStyle: Paint.Style = Paint.Style.FILL
    ): Paint = Paint(
        Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
    ).apply {
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
}