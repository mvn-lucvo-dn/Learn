package com.example.learnanything.room.first_demo.list

import com.example.learnanything.room.first_demo.data.source.database.entity.User
import kotlinx.coroutines.flow.StateFlow

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
interface UserInfoListVMContract {

    suspend fun getUsersFromDB()

    fun dataChangeObserver(): StateFlow<MutableList<User>>

    fun addUser(user: User)

    fun getUsers(): MutableList<User>

    fun deleteAll()
}