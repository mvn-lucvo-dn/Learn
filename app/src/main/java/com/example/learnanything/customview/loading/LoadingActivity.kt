package com.example.learnanything.customview.loading

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.databinding.ActivityLoadingBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class LoadingActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loading.startAnimation()
    }
}
