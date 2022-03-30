package com.example.learnanything.customview.horizontalscroll.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.learnanything.customview.horizontalscroll.dashboard.DashboardFragment

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class DashboardAdapter(
    fm: FragmentManager,
    private val titles: List<Int>,
    private val context: Context
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = titles.size

    override fun getPageTitle(position: Int) = context.getString(titles[position])

    override fun getItem(position: Int) = DashboardFragment()
}