package com.example.learnanything.customview.pathchart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.databinding.ActivityPathChartBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class PathChartActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPathChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPathChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.chart.setAnimation()
    }
}