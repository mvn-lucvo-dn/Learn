package com.example.learnanything.room.first_demo.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learnanything.room.first_demo.data.source.UserRepository

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class UserInfoViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserInfoViewModel(userRepository) as T
    }
}