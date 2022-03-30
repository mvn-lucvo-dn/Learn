package com.example.learnanything.hilt.firstdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.R
import com.example.learnanything.databinding.ActivityUserInfoBinding
import com.example.learnanything.extension.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
@AndroidEntryPoint
class DemoScopeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(R.id.frPersonContainer, UserFragment.newInstance())
    }
}