package com.example.learnanything.animation.motion

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class OnboardAdapter : PagerAdapter() {

    private val items = mutableListOf<OnBoardingPageView>()

    override fun getCount(): Int = items.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = items[position]
        container.addView(item)
        return item
    }

    fun setData(pages: List<OnBoardingPageView>) {
        this.items.clear()
        this.items.addAll(pages)
        notifyDataSetChanged()
    }
}