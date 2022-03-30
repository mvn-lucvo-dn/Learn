package com.example.learnanything.customview.piechartarc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.R
import com.example.learnanything.databinding.ActivityPiechartArcBinding
import com.example.learnanything.extension.makeVisible

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class PieChartArcActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPiechartArcBinding
    private var analysisInfoList = mutableListOf<AnalysisInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPiechartArcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analysisInfoList.add(
            AnalysisInfo(2000, R.drawable.ic_drink)
        )

        analysisInfoList.add(
            AnalysisInfo(2000, R.drawable.ic_electrical)
        )

        analysisInfoList.add(
            AnalysisInfo(2000, R.drawable.ic_add)
        )
        binding.pieChart.setData(
            analysisInfoList, 6000F
        )
    }
}
