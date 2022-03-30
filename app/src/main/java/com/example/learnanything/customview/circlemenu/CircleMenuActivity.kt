package com.example.learnanything.customview.circlemenu

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.databinding.ActivityCircleMenuBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class CircleMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCircleMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCircleMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}