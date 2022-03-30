package com.example.learnanything.hilt.firstdemo

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.learnanything.R
import com.example.learnanything.hilt.firstdemo.module.SourceScope
import com.example.learnanything.databinding.FragmentPersonDetailBinding
import com.example.learnanything.extension.addFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
@AndroidEntryPoint
class UserFragment : Fragment() {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var binding: FragmentPersonDetailBinding
    @Inject lateinit var sourceScope: SourceScope

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentPersonDetailBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        d("aaa", "$sourceScope")
    }

    private fun initViews() {
        binding.toolBar.run {
            setVisibleBack()

            onBackListener = {
                activity?.onBackPressed()
            }
        }

        binding.btnSubmit.setOnClickListener {
//            addChildFragment(R.id.containerPerson, UserListInfoFragment.newInstance(), UserListInfoFragment::class.java.name)
//            UserDialogFragment.newInstance().apply {
//                show(this@UserFragment.childFragmentManager, UserDialogFragment::class.java.name)
//            }
            (activity as? DemoScopeActivity)?.addFragment(
                R.id.frPersonContainer, newInstance(), backStackString = UserFragment::class.java.name
            )
        }
    }
}