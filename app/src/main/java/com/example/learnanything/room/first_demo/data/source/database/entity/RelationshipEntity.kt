package com.example.learnanything.room.first_demo.data.source.database.entity

import androidx.room.*

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class RelationshipEntity {

    /**
     * One - One
     */
    @Entity
    data class UserMusic(
        @PrimaryKey
        val userId: Long,
        val name: String,
        val age: Int
    )

    @Entity
    data class Library(
        @PrimaryKey
        val libraryId: Long,
        val userOwnerId: Long,
        val name:String
    )

    /**
     * Add the @Relation annotation to the instance of the child entity,
     * with parentColumn set to the name of the primary key column of the parent entity
     * entityColumn set to the name of the column of the child entity
     * that references the parent entity's primary key.
     */
    data class UserAndLibrary(
        @Embedded val userMusic: UserMusic,
        @Relation(
            parentColumn = "userId",
            entityColumn = "userOwnerId"
        )
        val library: Library
    )

    /**
     * One - N
     * One Parent entity has zero - many Child entity
     * But one child entity - One Parent Entity
     */

    @Entity
    data class Playlist(
        @PrimaryKey
        val playlistId: Long,
        val userCreatorId: Long,
        val playlistName: String
    )

    data class UserWithListPlaylist(
        @Embedded val userMusic: UserMusic,
        @Relation(
            parentColumn = "userId",
            entityColumn = "userCreatorId"
        )
        val playLists: List<Playlist>
    )

    /**
     * N - N
     */
    @Entity
    data class Song(
        @PrimaryKey
        val songId: Long,
        val songName: String
    )

    @Entity(primaryKeys = ["playlistId", "songId"])
    data class PlaylistSongCrossRef(
        val playlistId: Long,
        val songId: Long
    )

    /**
     * Query playlists and a list of the corresponding songs for each playlist
     * tell Room which table specify relation -> associateBy
     */
    data class PlaylistWithSongs(
        @Embedded val playList: Playlist,
        @Relation(
            parentColumn = "playlistId",
            entityColumn = "songId",
            associateBy = Junction(PlaylistSongCrossRef::class)
        )
        val songs: List<Song>
    )

    /**
     * Query songs and a list of the corresponding playlists for each
     */
    data class SongWithPlaylists(
        @Embedded val song: Song,
        @Relation(
            parentColumn = "songId",
            entityColumn = "playlistId",
            associateBy = Junction(PlaylistSongCrossRef::class)
        )
        val playLists: List<Playlist>
    )

    /**
     * Case Nested Relation
     */
     data class UserWithPlaylistsWithSongs(
        @Embedded val userId: Long,
        @Relation(
            entity = Playlist::class,
            parentColumn = "userId",
            entityColumn = "userCreatorId"
        )
        val playLists: List<PlaylistWithSongs>
     )
}