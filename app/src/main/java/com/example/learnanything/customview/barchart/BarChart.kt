package com.example.learnanything.customview.barchart

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnanything.R

class BarChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ValueAnimator.AnimatorUpdateListener {

    companion object {
        private const val TOTAL_COUNT = 10
        private const val PADDING = 20F
        private const val DEFAULT_STROKE_WIDTH = 5F
        private const val GUIDE_STROKE_WIDTH = 3F
    }

    private var padding = PADDING
    private var dataList = mutableListOf<Int>()
    private var animator: ValueAnimator? = null
    private var labelWidth = 0F
    private var animationFraction = 0F
    private var maxValue: Int? = null
    private var axisPaint: Paint
    private var guidePaint: Paint
    private var barPaint: Paint
    private var textPaint: Paint

    init {
        axisPaint = initPaint(R.color.black)
        guidePaint = initPaint(R.color.colorGray, stroke = GUIDE_STROKE_WIDTH)
        barPaint = initPaint(R.color.colorGray, paintStyle = Paint.Style.FILL)
        textPaint = initPaint(R.color.colorGray, paintStyle = Paint.Style.FILL).apply {
            textSize = 20 * (resources.displayMetrics.density)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawText(canvas)
        drawGuides(canvas)
        drawAxis(canvas)
        drawBars(canvas)
    }

    internal fun setData(dataList: MutableList<Int>) {
        this.dataList = dataList
        dataList.maxOrNull()?.let {
            maxValue = if (it % 10 > 0) {
                (it / 10) * 10 + 10
            } else {
                (it / 10) * 10
            }
            labelWidth = textPaint.measureText(maxValue.toString())
        }
        setAnimation()
    }

    private fun drawAxis(canvas: Canvas?) {
        // draw y
        canvas?.drawLine(
            padding + labelWidth,
            padding,
            padding + labelWidth,
            height - padding,
            axisPaint
        )
        // draw x
        canvas?.drawLine(
            padding + labelWidth,
            height - padding,
            width - padding,
            height - padding,
            axisPaint
        )
    }

    private fun drawGuides(canvas: Canvas?) {
        val heightGuide = ((height - 2 * padding) / TOTAL_COUNT)
        for (i in 0 until TOTAL_COUNT) {
            val height = (heightGuide * i) + padding
            canvas?.drawLine(padding + labelWidth, height, width - padding, height, guidePaint)
        }
    }

    private fun drawText(canvas: Canvas?) {
        val heightGuide = ((height - 2 * padding) / TOTAL_COUNT)
        maxValue?.let {
            for (i in 0 until TOTAL_COUNT) {
                val value = (it * (TOTAL_COUNT - i) / TOTAL_COUNT).toString()
                val rect = Rect()
                textPaint.getTextBounds(value, 0, value.length, rect)
                val height = (heightGuide * i) + padding + rect.height() / 2
                canvas?.drawText(value, 0f, height, textPaint)
            }
        }
    }

    private fun drawBars(canvas: Canvas?) {
        val totalSpace = padding * (dataList.size - 1)
        val widthBar = ((width - 2 * padding - totalSpace - labelWidth) / dataList.size)
        val height = height - 2 * padding
        maxValue?.let {
            for (i in 0 until dataList.size) {
                val value = dataList[i]
                val rect = RectF().apply {
                    left = (padding + widthBar) * i + padding + labelWidth
                    right = left + widthBar
                    bottom = height + padding
                    top = (height * (it - (value * animationFraction)) / it) + padding
                }
                canvas?.drawRect(rect, barPaint)
            }
        }
    }

    private fun setAnimation() {
        animator = ValueAnimator.ofFloat(0F, 1F).apply {
            duration = 3000L
            addUpdateListener(this@BarChart)
        }
        animator?.start()
    }

    private fun initPaint(
        colorRes: Int? = null,
        paintStyle: Paint.Style? = Paint.Style.STROKE,
        stroke: Float = DEFAULT_STROKE_WIDTH
    ): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            colorRes?.let {
                style = paintStyle
                strokeWidth = stroke
                color = ContextCompat.getColor(context, it)
            }
        }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        (animation?.animatedValue as? Float)?.let {
            animationFraction = it
            invalidate()
        }
    }
}
