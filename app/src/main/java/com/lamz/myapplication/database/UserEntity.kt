package com.lamz.myapplication.database

import androidx.room.*


@Entity(tableName = "MyFavorite")
data class UserEntity(
    @field:PrimaryKey
    @field:ColumnInfo(name = "username")
    var username: String = "",

    @field:ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null,

    @field:ColumnInfo(name = "favorite")
    var isFavorite: Boolean
)