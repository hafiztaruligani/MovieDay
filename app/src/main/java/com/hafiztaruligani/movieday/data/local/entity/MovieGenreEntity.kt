package com.hafiztaruligani.movieday.data.local.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.hafiztaruligani.movieday.domain.model.Genre

@Entity(tableName = "movie_genre")
data class MovieGenreEntity(
    @PrimaryKey(autoGenerate = false)
    val genreId: Int,
    val name: String
){
    @Ignore
    fun toGenre() =
        Genre(
            id = genreId,
            name = name
        )
}
