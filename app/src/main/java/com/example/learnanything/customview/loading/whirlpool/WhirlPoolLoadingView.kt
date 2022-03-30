package com.example.learnanything.customview.loading.whirlpool

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
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
class WhirlPoolLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context, attrs, defStyleAttr
) {

    companion object {
        private const val DURATION = 1000L
        private const val DEFAULT_SPACE = 20F
        private const val DEFAULT_AMOUNT = 3
        private const val DEFAULT_SIZE = 150F
        private const val DURATION_DELAY = 300L
    }

    private var desiredSize = DEFAULT_SIZE.dpToPx(context)
    private var space = DEFAULT_SPACE.dpToPx(context).toFloat()
    private var whirlPaint: Paint
    private var whirlRectF = RectF()
    private var translateMoves = MutableList(DEFAULT_AMOUNT) { 0 }

    init {
        whirlPaint = initPaint(R.color.white)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(desiredSize, widthMeasureSpec)
        val height = resolveSize(desiredSize, heightMeasureSpec)
        setMeasuredDimension(min(width, height), min(width, height))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        whirlRectF.apply {
            left = space
            right = w - space
            top = space
            bottom = h - space
        }
        whirlPaint.strokeWidth = (height - space * 2) / (DEFAULT_AMOUNT * DEFAULT_AMOUNT)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            for (i in 0 until DEFAULT_AMOUNT) {
                save()
                scale(
                    (DEFAULT_AMOUNT - i) / DEFAULT_AMOUNT.toFloat(),
                    (DEFAULT_AMOUNT - i) / DEFAULT_AMOUNT.toFloat(),
                    width / 2F, height / 2F
                )
                drawArc(whirlRectF, 45F * i + translateMoves[i], 90F, false, whirlPaint)
                restore()
            }
        }
    }

    internal fun startAnimation() {
        for (i in 0 until DEFAULT_AMOUNT) {
            val translateAnimation = ValueAnimator.ofInt(0, 360).apply {
                duration = DURATION
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
                startDelay = i * DURATION_DELAY
                addUpdateListener {
                    (it.animatedValue as? Int)?.let { value ->
                        translateMoves[i] = value
                        invalidate()
                    }
                }
            }
            translateAnimation.start()
        }
    }

    private fun initPaint(
        colorRes: Int? = null,
        style: Paint.Style = Paint.Style.STROKE
    ) = Paint(
        Paint.ANTI_ALIAS_FLAG and Paint.DITHER_FLAG
    ).apply {
        colorRes?.let {
            color = ContextCompat.getColor(context, it)
        }
        this.style = style
    }
}
