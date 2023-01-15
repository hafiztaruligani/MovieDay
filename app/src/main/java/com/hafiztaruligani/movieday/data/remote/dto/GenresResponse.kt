package com.hafiztaruligani.movieday.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GenresResponse(
	@field:SerializedName("genres")
	val genres: List<GenreResponse>
)