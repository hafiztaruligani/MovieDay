package com.hafiztaruligani.movieday.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.hafiztaruligani.movieday.data.local.entity.MovieGenreEntity
import com.hafiztaruligani.movieday.domain.model.Genre

data class GenreResponse(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("name")
	val name: String
){
	fun toGenreEntity(): MovieGenreEntity {
		return MovieGenreEntity(
			genreId = id,
			name = name
		)
	}

	fun toGenre():Genre{
		return Genre(
			id = id,
			name = name
		)
	}
}