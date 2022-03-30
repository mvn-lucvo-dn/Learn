package com.example.learnanything.animation

import android.animation.*
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.learnanything.R
import com.example.learnanything.databinding.ActivityAnimationBinding
import com.example.learnanything.extension.makeGone
import com.example.learnanything.extension.makeVisible


/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class AnimationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
        binding.root.layoutTransition = getLayoutTransaction()
    }

    private fun initListener() {
        binding.btnAnimate.setOnClickListener {
            binding.root.addView(
                TextView(this).apply {
                    text = "abcd"
                    textSize = 20F
                }
            )
        }

        binding.btnDelete.setOnClickListener {
            handleKeyFrames()
        }
    }


    /**
     * Use this fun to test LayoutTransaction
     */
    private fun updateVisibility() {
        if (binding.imgDog.isVisible) {
            binding.imgDog.makeGone()
        } else {
            binding.imgDog.makeVisible()
        }
    }

    private fun getLayoutTransaction(): LayoutTransition {
        val transition = LayoutTransition()

        /**
         * Animation when child View is added to Parent's View
         *
         */
        val appearAnim: Animator = ObjectAnimator
            .ofFloat(null, View.ROTATION_X, 90f, 0f)
            .setDuration(transition.getDuration(LayoutTransition.APPEARING))
        transition.setAnimator(LayoutTransition.APPEARING, appearAnim)

        /**
         * Animation when child View is removed from Parent's View
         *
         */
        val disappearAnim: Animator = ObjectAnimator
            .ofFloat(null, View.ROTATION_X, 0f, 90f)
            .setDuration(transition.getDuration(LayoutTransition.DISAPPEARING))
        transition.setAnimator(LayoutTransition.DISAPPEARING, disappearAnim)

        /**
         * Animation of other child views when the child View is added to Parent's View
         *  CHANGE_APPEARING if apply with View.ALPHA , first value and last value should be 1
         *  pvhLeft , pvhTop, or (pvhRight, pvhBottom) must be written, if no require value = 0
         */
        val pvhLeft = PropertyValuesHolder.ofInt("left", 0, 0)
        val pvhTop = PropertyValuesHolder.ofInt("top", 0, 0)
        val pvhRight = PropertyValuesHolder.ofInt("right", 0, 0)
        val pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 0)
        val pvhTranslationY = PropertyValuesHolder
            .ofFloat(View.ALPHA, 1F, 0F, 1F)
        val changeAppearAnim: Animator = ObjectAnimator
            .ofPropertyValuesHolder(
                binding.root,
                pvhLeft,
                pvhTop,
                pvhRight,
                pvhBottom,
                pvhTranslationY
            )
            .setDuration(1000L)
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING, changeAppearAnim)

        /**
         * Animation of other child views when the child view is removed from Parent's View
         *
         */
        val outLeft = PropertyValuesHolder.ofInt("left", 0, 0)
        val outTop = PropertyValuesHolder.ofInt("top", 0, 0)
        val pvhTranslationYDis = PropertyValuesHolder
            .ofFloat(View.TRANSLATION_X, 0f, -150f, 0f)
        val changeDisAppearAnim: ObjectAnimator = ObjectAnimator
            .ofPropertyValuesHolder(binding.root, outLeft, outTop, pvhTranslationYDis)
            .setDuration(transition.getDuration(LayoutTransition.CHANGE_DISAPPEARING))
        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeDisAppearAnim)
        return transition
    }

    private fun handleAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.demo_anim)
        animation.duration = 1000L
        binding.imgDog.startAnimation(animation)
    }

    /**
     * Keyframe specifies value at specific time of an animation
     */
    @SuppressLint("Recycle")
    private fun handleKeyFrames() {
        val kf0 = Keyframe.ofFloat(0F, 0F)
        val kf1 = Keyframe.ofFloat(0.5F, 180F)
        val kf2 = Keyframe.ofFloat(1F, 0F)
        val pvhRotation = PropertyValuesHolder.ofKeyframe(View.ROTATION, kf0, kf1, kf2)
        ObjectAnimator.ofPropertyValuesHolder(binding.imgDog, pvhRotation).apply {
            duration = 1000L
            start()
        }
    }
}