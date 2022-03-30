package com.example.learnanything.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.learnanything.databinding.CustomToolbarBinding
import com.example.learnanything.extension.makeVisible

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class CustomToolBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: CustomToolbarBinding =
        CustomToolbarBinding.inflate(LayoutInflater.from(context), this, false)
    internal var onBackListener: () -> Unit = {}
    internal var onDeleteListener: () -> Unit = {}

    init {
        initView()
        binding.imgBack.setOnClickListener {
            onBackListener.invoke()
        }

        binding.imgDelete.setOnClickListener {
            onDeleteListener.invoke()
        }
    }

    internal fun setVisibleTitle() {
        binding.tvTitle.makeVisible()
    }

    internal fun setVisibleBack() {
        binding.imgBack.makeVisible()
    }

    internal fun setVisibleDelete() {
        binding.imgDelete.makeVisible()
    }

    private fun initView() {
        val set = ConstraintSet()
        binding.root.id = View.generateViewId()
        addView(binding.root, 0)
        set.clone(this)
        set.connect(binding.root.id, ConstraintSet.TOP, id, ConstraintSet.TOP)
        set.connect(binding.root.id, ConstraintSet.START, id, ConstraintSet.START)
        set.applyTo(this)
    }
}
