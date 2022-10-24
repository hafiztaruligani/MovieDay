package com.hafiztaruligani.movieday.data.local.entity

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.Relation
import com.hafiztaruligani.movieday.domain.model.GenreWithMovies
import com.hafiztaruligani.movieday.domain.model.MovieSimple

data class GenreWithMoviesEntity (
    @Embedded
    val genre: MovieGenreEntity,
    @Relation(
        parentColumn = "genreId",
        entityColumn = "movieId",
        associateBy = Junction(GenreMovieCrossRef::class)
    )
    val movies : List<MovieSimpleEntity>
){
    @Ignore
    fun toGenreWithMovies(): GenreWithMovies{
        return GenreWithMovies(
            genre = genre.toGenre(),
            movies = movies.sortedByDescending { it.releaseDate }.map { it.toMovieSimple() }
        )
    }
}
