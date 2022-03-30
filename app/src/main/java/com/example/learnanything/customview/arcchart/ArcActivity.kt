package com.example.learnanything.customview.arcchart

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.databinding.ActivityArcChartBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class ArcActivity: AppCompatActivity() {

    private lateinit var binding: ActivityArcChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArcChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.arcChart.setData(90F, 250F)
    }
}