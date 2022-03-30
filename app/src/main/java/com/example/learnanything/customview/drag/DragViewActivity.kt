package com.example.learnanything.customview.drag

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.databinding.ActivityDragBinding
import com.example.learnanything.databinding.ItemInfoBinding
import java.util.*


/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class DragViewActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_AMOUNT_VISIBLE = 2
        private const val DEFAULT_DEGREE = 30F
    }

    private lateinit var binding: ActivityDragBinding
    private var listViewInfo = mutableListOf<View>()
    private var firstX = 0F
    private var offset = 0F
    private var dX: Float = 0f
    private var isMoving = false
    private var location = IntArray(2)
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDragBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initListeners()
    }

    private fun initViews() {
        for (i in 0 until DEFAULT_AMOUNT_VISIBLE) {
            getViewInfo().apply {
                listViewInfo.add(this)
                binding.frContainerDrag.addView(this)
            }
        }
        /**
         * Move view to top of children
         */
        listViewInfo.first().bringToFront()
    }

    private fun initListeners() {
        binding.btnDrag.setOnClickListener {
            dragViews()
        }
    }

    private fun dragViews() {
        if (listViewInfo.size > 0) {
            binding.frContainerDrag.removeView(
                listViewInfo.first()
            )
            listViewInfo.remove(listViewInfo.first())
        }
        if (listViewInfo.size == 1) {
            val currentView = listViewInfo.first()
            val index = binding.frContainerDrag.indexOfChild(currentView)
            val nextView = getViewInfo()
            binding.frContainerDrag.addView(nextView, index)
            listViewInfo.add(nextView)
        }
    }

    private fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getViewInfo(): View {
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val infoView = ItemInfoBinding.inflate(LayoutInflater.from(this), binding.root, false)
        infoView.tvIndex.text = index.toString()
        index++
        return infoView.root.apply {
            setBackgroundColor(getRandomColor())
            this.layoutParams = layoutParams
            setOnTouchListener { view, event ->
                handleMoveView(view, event)
                false
            }
        }
    }


    private fun handleMoveView(view: View, event: MotionEvent?) {
        view.pivotY = view.height.toFloat()
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                firstX = event.rawX
                isMoving = false
            }
            MotionEvent.ACTION_MOVE -> {
                dX = (event.rawX - firstX)
                view.getLocationOnScreen(location)
                if (dX > 0 || location[0] >= 0) {
                    isMoving = true
                    offset = dX / view.width
                    view.animate()
                        .rotation(DEFAULT_DEGREE * offset)
                        .translationX(dX)
                        .translationY(
                            (dX / 2) * (offset)
                        )
                        .setDuration(0).start()
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isMoving) {
                    if (offset > 0.6) {
                        view.animate().alpha(0F).translationX(view.width - event.x)
                            .setDuration(200L).apply {
                                setListener(object : Animator.AnimatorListener {
                                    override fun onAnimationStart(animation: Animator?) {
                                        //no-op
                                    }

                                    override fun onAnimationEnd(animation: Animator?) {
                                        dragViews()
                                    }

                                    override fun onAnimationCancel(animation: Animator?) {
                                        //no-op
                                    }

                                    override fun onAnimationRepeat(animation: Animator?) {
                                        //no-op
                                    }

                                })
                            }
                    } else {
                        val rotate =
                            ObjectAnimator.ofFloat(view, View.ROTATION, DEFAULT_DEGREE * offset, 0F)
                                .apply {
                                    duration = 100L
                                }

                        val translateX =
                            ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0F).apply {
                                duration = 100L
                            }
                        val translateY =
                            ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0F).apply {
                                duration = 100L
                            }
                        AnimatorSet().apply {
                            play(rotate).with(translateX).with(translateY)
                            start()
                        }
                    }
                }
            }
        }
    }
}