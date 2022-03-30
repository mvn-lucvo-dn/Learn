package com.example.learnanything.animation.motion

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.learnanything.databinding.ActivityOnboardingBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initListeners()
    }

    private fun initListeners() {
        binding.pagesList.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (adapter.count > 1) {
                    binding.onBoardingRoot.progress =
                        (position + positionOffset) / (adapter.count - 1)
                }
            }

            override fun onPageSelected(position: Int) {
                //No-op
            }

            override fun onPageScrollStateChanged(state: Int) {
                //No-op
            }

        })
    }

    private fun initViews() {
        adapter = OnboardAdapter()
        adapter.setData(
            OnboardingPage.values().map {
                val pageView = OnBoardingPageView(this)
                pageView.setPageData(it)
                pageView
            }
        )
        binding.pagesList.adapter = adapter
    }
}