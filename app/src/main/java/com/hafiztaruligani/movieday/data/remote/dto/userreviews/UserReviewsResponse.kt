package com.hafiztaruligani.movieday.data.remote.dto.userreviews

import com.google.gson.annotations.SerializedName

data class UserReviewsResponse(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("results")
	val userReviewItemResponses: List<UserReviewItemResponse?>? = null,

	@field:SerializedName("total_results")
	val totalResults: Int? = null
)