package com.example.learnanything.animation.motion

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.learnanything.databinding.ItemOnboardingPageBinding

class OnBoardingPageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var page: OnboardingPage
    private lateinit var binding: ItemOnboardingPageBinding

    init {
        initUi()
    }

    private fun initUi() {
        binding = ItemOnboardingPageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setPageData(onboardPage: OnboardingPage) {
        this.page = onboardPage
        with(binding) {
            platformLogo.setImageResource(onboardPage.logoResource)
            pageTitle.text = context.getString(page.titleResource)
            pageDescription.text = context.getString(page.descriptionResource)
        }
    }
}