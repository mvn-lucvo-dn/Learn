package com.example.learnanything.hilt.firstdemo

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.learnanything.hilt.firstdemo.module.SourceScope
import com.example.learnanything.databinding.FragmentUserDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
@AndroidEntryPoint
class UserDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() = UserDialogFragment()
    }

    private lateinit var binding: FragmentUserDialogBinding
    @Inject lateinit var sourceScope: SourceScope

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDialogBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        d("aaa", "$sourceScope")
    }
}