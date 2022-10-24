package com.hafiztaruligani.movieday.data.local.entity.pagingentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "remote_key")
data class RemoteKeyEntity (
    @PrimaryKey (autoGenerate = false)
    val id: String,
    val nextPage: Int?
)