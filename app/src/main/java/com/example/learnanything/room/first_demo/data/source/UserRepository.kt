package com.example.learnanything.room.first_demo.data.source

import android.content.Context
import com.example.learnanything.room.first_demo.data.source.database.UserDatabase
import com.example.learnanything.room.first_demo.data.source.database.entity.User
import com.example.learnanything.room.first_demo.data.source.datasource.UserDataSource

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class UserRepository(var context: Context) : UserDataSource {

    private var userDatabase = UserDatabase.invoke(context)

    override suspend fun getUserInfo(id: Int) = userDatabase.getUserDao().getUserInfo(id)

    override suspend fun getListUserInfo() = userDatabase.getUserDao().getListUserInfo()

    override suspend fun addUser(user: User){
        userDatabase.getUserDao().addUser(user)
    }

    override suspend fun updateUser(user: User) {
        userDatabase.getUserDao().updateUser(user)
    }

    override suspend fun deleteAll() {
        userDatabase.getUserDao().deleteAll()
    }

    override suspend fun deleteUser(id: Int) {
        userDatabase.getUserDao().deleteUser(id)
    }
}