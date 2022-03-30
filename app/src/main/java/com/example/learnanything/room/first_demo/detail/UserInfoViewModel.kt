package com.example.learnanything.room.first_demo.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnanything.room.first_demo.data.source.UserRepository
import com.example.learnanything.room.first_demo.data.source.database.entity.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class UserInfoViewModel(private val userRepository: UserRepository) : ViewModel(),
    UserInfoVMContract {

    override fun addUser(user: User) {
        viewModelScope.launch(IO) {
            userRepository.addUser(user)
        }
    }

    override fun editUser(user: User) {
        viewModelScope.launch(IO) {
            userRepository.updateUser(user)
        }
    }

    override suspend fun getUserInfo(id: Int) = userRepository.getUserInfo(id)

    override fun isEmptyField(name: String, age: String) =
        name.isEmpty() && age.isEmpty()
}