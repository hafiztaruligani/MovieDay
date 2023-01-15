package com.hafiztaruligani.movieday.data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "genre_movie_cross_ref",
    primaryKeys = ["genreId","movieId"],
    indices = [Index("genreId"), Index("movieId")]
)
data class GenreMovieCrossRef(
    val genreId: Int,
    val movieId: Int
)
