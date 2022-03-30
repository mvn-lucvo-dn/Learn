package com.example.learnanything.customview.loading.sharingan

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
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
class SharinganLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context, attrs, defStyleAttr
) {

    companion object {
        private const val DEFAULT_STROKE = 20F
        private const val DEFAULT_SIZE = 150F
        private const val DEFAULT_AMOUNT = 3
        private const val DEFAULT_SPACE_EYES = 120F
    }

    private var eyePaint: Paint
    private var boundEyePaint: Paint
    private var defaultSize = DEFAULT_SIZE.dpToPx(context)
    private var radius = 0F
    private var bigRadius = 0F
    private var sharinganAmount = DEFAULT_AMOUNT
    private var rotate = 1F
    private var scale = 1F

    init {
        eyePaint = initPaint(
            colorRes = R.color.white
        )
        boundEyePaint = initPaint(
            colorRes = R.color.white,
            style = Paint.Style.STROKE,
            widthStroke = DEFAULT_STROKE
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(defaultSize, widthMeasureSpec)
        val height = resolveSize(defaultSize, heightMeasureSpec)
        setMeasuredDimension(min(width, height), min(width, height))
        radius = width.toFloat() / 8
        bigRadius = (width / 2F) - radius
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val halfWidth = width / 2F
        val halfHeight = height / 2F
        canvas?.apply {
            save()
            rotate(rotate, halfWidth, halfHeight)
            scale(scale, scale, halfWidth, halfHeight)
            drawCenterEye(canvas)
            drawEyeBound(canvas)
            drawSharinganEyes(canvas)
            restore()
        }
    }

    internal fun startAnimation() {
        val rotationAnimator = ValueAnimator.ofFloat(0F, 360F)
        rotationAnimator.apply {
            duration = 2000L
            repeatCount = ValueAnimator.INFINITE
        }
        rotationAnimator.addUpdateListener {
            (it.animatedValue as? Float)?.let { value ->
                rotate = value
                invalidate()
            }
        }
        /**
         * Use 3 points (1F, 0.8F, 1F) to ensure view return begin start animation
         */
        val scaleAnimator = ValueAnimator.ofFloat(1F, 0.8F, 1F)
        scaleAnimator.apply {
            duration = 2000L
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateInterpolator()
        }
        scaleAnimator.addUpdateListener {
            (it.animatedValue as? Float)?.let { value ->
                scale = value
                invalidate()
            }
        }
        val animationSet = AnimatorSet()
        animationSet.play(scaleAnimator).with(rotationAnimator)
        animationSet.start()
    }

    private fun drawSharinganEyes(canvas: Canvas?) {
        /**
         * instead of calculating coordinates x, y , use Rotate to make easy
         */
        canvas?.apply {
            for (i in 0 until sharinganAmount) {
                save()
                rotate(i * DEFAULT_SPACE_EYES, width / 2F, height / 2F)
                drawCircle(width / 2F, (height / 2F) - bigRadius, radius, eyePaint)
                restore()
            }
        }
    }

    private fun drawEyeBound(canvas: Canvas?) {
        canvas?.drawCircle(
            width / 2F, height / 2F, bigRadius, boundEyePaint
        )
    }

    private fun drawCenterEye(canvas: Canvas?) {
        canvas?.drawCircle(
            width / 2F, height / 2F, radius, eyePaint
        )
    }

    private fun initPaint(
        colorRes: Int? = null,
        style: Paint.Style = Paint.Style.FILL,
        widthStroke: Float? = null
    ) = Paint(
        Paint.ANTI_ALIAS_FLAG and Paint.DITHER_FLAG
    ).apply {
        colorRes?.let {
            color = ContextCompat.getColor(context, it)
        }
        widthStroke?.let {
            strokeWidth = widthStroke
        }
        this.style = style
    }
}