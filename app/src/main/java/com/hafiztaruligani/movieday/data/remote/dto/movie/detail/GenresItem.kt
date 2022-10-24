package com.hafiztaruligani.movieday.data.remote.dto.movie.detail

import com.google.gson.annotations.SerializedName
import com.hafiztaruligani.movieday.domain.model.Genre

data class GenresItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
){
	fun toGenre() =
		Genre(
			id = id?:0,
			name = name?:"-"
		)
}