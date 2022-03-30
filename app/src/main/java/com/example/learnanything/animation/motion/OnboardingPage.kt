package com.example.learnanything.animation.motion

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.learnanything.R

enum class OnboardingPage(
    @StringRes val titleResource: Int,
    @StringRes val descriptionResource: Int,
    @DrawableRes val logoResource: Int
) {

    ANDROID(R.string.android_title, R.string.android_description, R.drawable.android_logo),
    IOS(R.string.ios_title, R.string.ios_description, R.drawable.ios_swift_logo),
    UNITY(R.string.unity_title, R.string.unity_description, R.drawable.ios_swift_logo),
    KOTLIN(R.string.kotlin_title, R.string.kotlin_description, R.drawable.kotlin_logo),
    SWIFT(R.string.swift_title, R.string.swift_description, R.drawable.ios_swift_logo)
}