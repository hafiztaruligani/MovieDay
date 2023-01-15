package com.hafiztaruligani.movieday.presentation.movies

import com.hafiztaruligani.movieday.domain.model.GenreWithMovies

data class MoviesUiState (
    val loading : Boolean = false,
    val error : String = "",
    val data: List<GenreWithMovies> = listOf()
)