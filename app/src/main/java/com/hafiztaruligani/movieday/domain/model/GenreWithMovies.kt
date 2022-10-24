package com.hafiztaruligani.movieday.domain.model

data class GenreWithMovies(
    val genre: Genre,
    val movies: List<MovieSimple>
)
