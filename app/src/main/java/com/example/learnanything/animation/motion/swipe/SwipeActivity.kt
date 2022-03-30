package com.example.learnanything.animation.motion.swipe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.databinding.ActivitySwipeBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class SwipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySwipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}