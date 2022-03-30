package com.example.learnanything.room.first_demo.data.source.database

import androidx.room.*
import com.example.learnanything.room.first_demo.data.source.database.entity.User
import kotlinx.coroutines.flow.Flow

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user WHERE id == :id ")
    fun getUserInfo(id: Int): Flow<User>

    @Query("SELECT * FROM user")
    fun getListUserInfo(): Flow<MutableList<User>>

    @Query("DELETE FROM sqlite_sequence WHERE name ='SequenceAction' ")
    suspend fun deleteAll()

    @Query("DELETE FROM user WHERE id == :id")
    suspend fun deleteUser(id: Int)
}