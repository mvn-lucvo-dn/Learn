package com.example.learnanything.room.first_demo.data.source.datasource

import com.example.learnanything.room.first_demo.data.source.database.entity.User
import kotlinx.coroutines.flow.Flow

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
interface UserDataSource {

    suspend fun getUserInfo(id: Int): Flow<User>

    suspend fun getListUserInfo(): Flow<MutableList<User>>

    suspend fun addUser(user: User)

    suspend fun deleteAll()

    suspend fun deleteUser(id: Int)

    suspend fun updateUser(user: User)
}