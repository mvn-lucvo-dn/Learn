package com.example.learnanything.room.first_demo.detail

import com.example.learnanything.room.first_demo.data.source.database.entity.User
import kotlinx.coroutines.flow.Flow

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
interface UserInfoVMContract {

    fun addUser(user: User)

    fun editUser(user: User)

    fun isEmptyField(name: String, age: String): Boolean

    suspend fun getUserInfo(id: Int): Flow<User>
}