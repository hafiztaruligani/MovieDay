package com.hafiztaruligani.movieday.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieSimple (
    val id: Int,
    val title : String,
    val releaseDate : String,
    val voteAverage : Double,
    val poster : String,
    val genresName : List<String>
) : Parcelable