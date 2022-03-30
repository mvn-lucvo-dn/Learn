package com.example.learnanything.hilt.firstdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.learnanything.hilt.firstdemo.module.SourceScope
import com.example.learnanything.databinding.FragmentUserListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
@AndroidEntryPoint
class UserListInfoFragment : Fragment() {

    companion object {
        fun newInstance() = UserListInfoFragment()
    }

    private lateinit var binding: FragmentUserListBinding
    @Inject lateinit var sourceScope: SourceScope


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        d("aaa", "$sourceScope")
    }

}