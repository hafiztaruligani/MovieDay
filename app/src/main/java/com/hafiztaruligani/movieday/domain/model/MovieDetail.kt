package com.hafiztaruligani.movieday.domain.model

import java.math.RoundingMode

data class MovieDetail(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val runtime: String,
    val videos: List<String>,
    val poster: String,
    val genresName: List<String>,
    val overview: String,
    val voteAverage: String,
    val voteCount: String
){

}