package com.example.learnanything.room.first_demo.data.source.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.learnanything.room.first_demo.data.source.database.entity.RelationshipEntity

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
@Dao
interface RelationshipDao {

    /**
     * Avoid Multiple Thread Issue We need to add @Transaction
     * When room is executing the first query by the way another query just change database
     * but Room doesn't know that, so to be ensure prevent issue that we need to add @Transaction
     */
    @Transaction
    @Query("SELECT * FROM usermusic")
    suspend fun getUserAndLibrary(): MutableList<RelationshipEntity.UserAndLibrary>

    @Transaction
    @Query("SELECT * FROM usermusic")
    suspend fun getUsersWithPlaylists(): MutableList<RelationshipEntity.UserWithListPlaylist>

    @Transaction
    @Query("SELECT * FROM playlist")
    suspend fun getPlaylistsWithSongs(): MutableList<RelationshipEntity.PlaylistWithSongs>

    @Transaction
    @Query("SELECT * FROM song")
    suspend fun getSongsWithPlaylists(): MutableList<RelationshipEntity.SongWithPlaylists>

    @Transaction
    @Query("SELECT * FROM user")
    suspend fun getUsersWithPlaylistsSongs(): MutableList<RelationshipEntity.UserWithPlaylistsWithSongs>
}