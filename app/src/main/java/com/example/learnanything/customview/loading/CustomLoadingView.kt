package com.example.learnanything.customview.loading

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnanything.R
import com.example.learnanything.customview.pathchart.PathChart

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class CustomLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_STROKE_WIDTH = 20F
        private const val START_ANGLE = 270F
        private const val SWEEP_ANGLE = 360F
        private const val LOADING_TITLE = "loading"
    }

    private var loadingPaint: Paint
    private var textPaint: Paint
    private var sweepAngle = SWEEP_ANGLE
    private var animationValue = 1F

    init {
        loadingPaint = initPaint(R.color.black, widthStroke = DEFAULT_STROKE_WIDTH)
        textPaint = initPaint(R.color.black).apply {
            textAlign = Paint.Align.CENTER
            textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20F,
                context.resources.displayMetrics
            )
        }

        val animation = ValueAnimator.ofFloat(0F, 1F).apply {
            duration = 2000L
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                (it?.animatedValue as? Float)?.let { value ->
                    this@CustomLoadingView.animationValue = value
                }
                invalidate()
            }
        }
        animation.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawProgress(canvas)
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas?) {
        canvas?.drawText(
            LOADING_TITLE, (width/2).toFloat() , (height.toFloat()/2), textPaint
        )
    }

    private fun drawProgress(canvas: Canvas?) {
        canvas?.drawArc(
            RectF(
                0F + DEFAULT_STROKE_WIDTH / 2,
                0F + DEFAULT_STROKE_WIDTH / 2,
                width.toFloat() - DEFAULT_STROKE_WIDTH / 2,
                height.toFloat() - DEFAULT_STROKE_WIDTH / 2
            ), sweepAngle* animationValue -20F, sweepAngle * animationValue, false, loadingPaint
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