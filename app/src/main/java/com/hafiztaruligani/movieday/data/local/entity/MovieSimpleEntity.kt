package com.hafiztaruligani.movieday.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hafiztaruligani.movieday.domain.model.MovieSimple
import com.hafiztaruligani.movieday.util.convertIntoList
import com.hafiztaruligani.movieday.util.toStringDate

@Entity(tableName = "movies_simple")
data class MovieSimpleEntity (
        @PrimaryKey (autoGenerate = false)
        val movieId : Int,
        val title : String,
        val releaseDate : String,
        val voteAverage : Double,
        val poster : String,
        val genresName : String
) {
        fun toMovieSimple() =
                MovieSimple(
                        id = movieId,
                        title = title,
                        releaseDate = releaseDate.toStringDate(),
                        voteAverage = voteAverage,
                        poster = poster,
                        genresName = genresName.convertIntoList()
                )
}
