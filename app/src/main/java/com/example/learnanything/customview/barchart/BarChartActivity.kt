package com.example.learnanything.customview.barchart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.databinding.ActivityBarchartBinding
import kotlin.random.Random

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class BarChartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBarchartBinding
    private var dataList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBarchartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for (i in 0 until 10) {
            val number = Random.nextInt(10, 100)
            dataList.add(number)
        }
        binding.barchart.setData(dataList)
    }
}