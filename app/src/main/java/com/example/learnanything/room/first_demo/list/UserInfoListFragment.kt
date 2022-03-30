package com.example.learnanything.room.first_demo.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.learnanything.R
import com.example.learnanything.room.first_demo.UserInfoActivity
import com.example.learnanything.room.first_demo.data.source.UserRepository
import com.example.learnanything.room.first_demo.detail.UserInfoFragment
import com.example.learnanything.databinding.FragmentUserListBinding
import com.example.learnanything.extension.addFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class UserInfoListFragment : Fragment() {

    companion object {
        fun newInstance() = UserInfoListFragment()
    }

    private lateinit var viewModel: UserInfoListViewModel
    private lateinit var binding: FragmentUserListBinding
    private lateinit var adapter: UserInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            UserInfoViewModelFactory(UserRepository(requireContext()))
        )[UserInfoListViewModel::class.java]
    }

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
        initViews()
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.getUsersFromDB()
            }
            launch {
                viewModel.dataChangeObserver().collect {
                    adapter.notifyDataSetChanged()
                }
            }
        }
        binding.fabAddUser.setOnClickListener {
            (activity as? UserInfoActivity)?.addFragment(
                R.id.frPersonContainer,
                UserInfoFragment.newInstance(),
                backStackString = UserInfoFragment::class.java.name
            )
        }
    }

    private fun initViews() {
        //recyclerview
        adapter = UserInfoAdapter(viewModel.getUsers())
        binding.recyclerViewPerson.adapter = adapter
        adapter.onClickDetailPerson = {
            (activity as? UserInfoActivity)?.addFragment(
                R.id.frPersonContainer,
                UserInfoFragment.newInstance(it.toString()),
                backStackString = UserInfoFragment::class.java.name
            )
        }

        //Toolbar
        binding.toolBar.run {
            setVisibleTitle()
            setVisibleDelete()
            onDeleteListener = {
                viewModel.deleteAll()
            }
        }
    }
}