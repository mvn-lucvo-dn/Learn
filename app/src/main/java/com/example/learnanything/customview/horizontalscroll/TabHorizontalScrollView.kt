package com.example.learnanything.customview.horizontalscroll

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log.d
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.learnanything.R
import com.example.learnanything.extension.dpToPx
import java.util.*

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class TabHorizontalScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_TEXT_SIZE = 12F
    }

    private var tabStrips = SlidingTabStrip(context)
    private lateinit var viewPager: ViewPager
    private var tabTextSize = DEFAULT_TEXT_SIZE.dpToPx(context)
    private var lastX = 0F
    private val isRtl =
        TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL

    init {
        setOnTouchListener { _, event ->
            handleOnTouchEvent(event)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val totalHeight = tabStrips.measuredHeight
        if (tabStrips.childCount > 0) {
            val firstTab = tabStrips.getChildAt(0)
            val lastTab = tabStrips.getChildAt(tabStrips.childCount - 1)
            val start = (w - firstTab.measuredWidth) / 2
            val end = (w - lastTab.measuredWidth) / 2
            setPaddingRelative(start, 0, end, 0)
            clipToPadding = false
        }


        for (i in 0 until tabStrips.childCount) {
            val currentTab = tabStrips.getChildAt(i)
            layoutParams = if (i <= tabStrips.childCount - 2) {
                val nextTab = tabStrips.getChildAt(i + 1).apply { }
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, totalHeight
                ).apply {
                    setMargins(
                        0,
                        0,
                        (w - (currentTab.measuredWidth + nextTab.measuredWidth)) / 2,
                        0
                    )
                }
            } else {
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, totalHeight
                )
            }
            currentTab.layoutParams = layoutParams
            tabStrips.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        }
    }

    internal fun setUpWithViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        val adapter = viewPager.adapter
        adapter?.let { it ->
            // First we'll add Tabs, using the adapter's page titles
            setTabsFromPagerAdapter(it)
        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                //No-op
            }

            override fun onPageScrollStateChanged(state: Int) {
                //No-op
            }

        })
    }

    private fun setTabsFromPagerAdapter(adapter: PagerAdapter) {
        removeAllViews()
        for (i in 0 until adapter.count) {
            setTitleView(adapter.getPageTitle(i).toString())
        }
        isFillViewport = false
        addView(tabStrips, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTitleView(title: String) {
        if (title.isNotEmpty()) {
            val titleView = TextView(context).apply {
                text = title
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize.toFloat())
                gravity = Gravity.CENTER
                includeFontPadding = false
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(ContextCompat.getColor(context, R.color.colorMarker))
            }
            tabStrips.addView(titleView)
        }
    }

    private fun handleOnTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                d("aaa", "down")
                lastX = event.rawX
                viewPager.beginFakeDrag()
            }

            MotionEvent.ACTION_MOVE -> {
                val value = event.rawX
                val delta = value - lastX
                d("aaa", "${mirrorInRtl(delta)}")
                viewPager.fakeDragBy(delta/width)
//                lastX = value
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                viewPager.endFakeDrag()
            }
        }
        return true
    }

    private fun mirrorInRtl(f: Float): Float {
        return if (isRtl) -f else f
    }

    private fun getValue(event: MotionEvent): Float {
        return mirrorInRtl(event.x)
    }
}
