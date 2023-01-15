package com.hafiztaruligani.movieday.data.remote.dto.movie

import com.google.gson.annotations.SerializedName

data class MovieDiscoveryResponse(

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("results")
	val movies: List<MovieSimpleResponse>,

	@field:SerializedName("total_results")
	val totalResults: Int? = null
)