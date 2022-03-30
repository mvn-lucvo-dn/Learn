package com.example.learnanything.room.first_demo.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnanything.room.first_demo.data.source.UserRepository
import com.example.learnanything.room.first_demo.data.source.database.entity.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class UserInfoListViewModel(private var userRepository: UserRepository) : ViewModel(),
    UserInfoListVMContract {

    private var users = mutableListOf<User>()
    private var mutableStateFlow = MutableStateFlow<MutableList<User>>(mutableListOf())

    override fun dataChangeObserver() = mutableStateFlow.asStateFlow()

    override suspend fun getUsersFromDB() {
        userRepository.getListUserInfo().flowOn(IO).collectLatest {
            users.clear()
            users.addAll(it)
            mutableStateFlow.value = it
        }
    }

    override fun addUser(user: User) {
        viewModelScope.launch(IO) {
            userRepository.addUser(user)
            getUsersFromDB()
        }
    }

    override fun deleteAll() {
        viewModelScope.launch(IO) {
            userRepository.deleteAll()
            getUsersFromDB()
        }
    }

    override fun getUsers() = users
}