package com.example.learnanything.customview.horizontalscroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.R
import com.example.learnanything.customview.horizontalscroll.adapter.DashboardAdapter
import com.example.learnanything.databinding.ActivityHorizontalBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class HorizontalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHorizontalBinding
    private lateinit var adapter: DashboardAdapter
    private var titles = mutableListOf(
        R.string.demo_tab_1,
        R.string.demo_tab_2,
        R.string.demo_tab_3,
        R.string.demo_tab_4,
        R.string.demo_tab_5,
        R.string.demo_tab_6,
        R.string.demo_tab_7,
        R.string.demo_tab_8,
        R.string.demo_tab_9,
        R.string.demo_tab_10
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHorizontalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        adapter = DashboardAdapter(supportFragmentManager, titles, this)
        binding.viewPager.adapter = adapter
        binding.tabHorizontal.setUpWithViewPager(binding.viewPager)
    }
}