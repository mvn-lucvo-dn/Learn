package com.example.learnanything.collapsingtoolbar

import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.databinding.ActivityCoordinatorlayoutBinding
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class CollapsingToolbarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoordinatorlayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoordinatorlayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScroll = appBarLayout.totalScrollRange
            d("aaa", "${abs(verticalOffset)}")
            val percent = abs(verticalOffset)/totalScroll.toFloat()
            if (percent >= 0.7F){
                binding.toolBar.visibility = View.VISIBLE
            } else {
                binding.toolBar.visibility = View.GONE
            }
        })
    }
}
