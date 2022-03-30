package com.example.learnanything.room.first_demo.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.learnanything.room.first_demo.data.source.UserRepository
import com.example.learnanything.room.first_demo.data.source.database.entity.User
import com.example.learnanything.databinding.FragmentPersonDetailBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class UserInfoFragment : Fragment() {

    companion object {
        fun newInstance(id: String? = null) = UserInfoFragment().apply {
            arguments = Bundle().apply {
                id?.let {
                    putString(KEY_ID, it)
                }
            }
        }

        private const val KEY_ID = "KEY_ID"
    }

    private lateinit var binding: FragmentPersonDetailBinding
    private lateinit var viewmodel: UserInfoVMContract
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(
            this, UserInfoViewModelFactory(
                UserRepository(requireContext())
            )
        )[UserInfoViewModel::class.java]
        arguments?.run {
            id = getString(KEY_ID)?.toIntOrNull()
        }
    }

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
        id?.let { id ->
            lifecycleScope.launch {
                viewmodel.getUserInfo(id).collect {
                    binding.edtName.setText(it.name)
                    binding.edtAge.setText(it.age.toString())
                }
            }
        }
        binding.btnSubmit.setOnClickListener {
            val name = binding.edtName.text.toString()
            val age = binding.edtAge.text.toString()
            if (!viewmodel.isEmptyField(name, age)) {
                handleUpdateOrAddData(name, age)
            }
        }
    }

    private fun handleUpdateOrAddData(name: String, age: String) {
        if (id == null) {
            viewmodel.addUser(User(name, age.toIntOrNull() ?: 0))
        } else {
            val user = User(name, age.toIntOrNull() ?: 0).apply {
                id = this@UserInfoFragment.id ?: 0
            }
            viewmodel.editUser(user)
        }
        activity?.onBackPressed()
    }

    private fun initViews() {
        binding.toolBar.run {
            setVisibleBack()

            onBackListener = {
                activity?.onBackPressed()
            }
        }
    }
}