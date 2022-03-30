package com.example.learnanything.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.learnanything.R
import com.example.learnanything.databinding.ActivityDefaultBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class ThirdActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDefaultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDefaultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))

        binding.btnShow.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java).apply {
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        d("aaa", "aaa")
    }
}