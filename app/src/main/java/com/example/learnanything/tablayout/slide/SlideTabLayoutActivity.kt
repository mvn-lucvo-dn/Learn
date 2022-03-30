package com.example.learnanything.tablayout.slide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.databinding.ActivitySlideTabBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class SlideTabLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlideTabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlideTabBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}