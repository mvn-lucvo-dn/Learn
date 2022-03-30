package com.example.learnanything.customview.piechart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.databinding.ActivityPiechartBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class PieChartActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPiechartBinding
    private var costInfoList = mutableListOf<CostInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPiechartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        costInfoList.add(
            CostInfo("Eat", 2000)
        )
        costInfoList.add(
            CostInfo("Drink", 2000)
        )
        costInfoList.add(
            CostInfo("Shopping", 2000)
        )

        binding.pieChart.setData(
            costInfoList
        )
    }
}
