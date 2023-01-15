package com.hafiztaruligani.movieday.data.remote.dto.userreviews

import com.google.gson.annotations.SerializedName
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.UserReviewEntity
import com.hafiztaruligani.movieday.domain.model.UserReview

data class UserReviewItemResponse(

	@field:SerializedName("author_details")
	val authorDetails: AuthorDetails? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("url")
	val url: String? = null
){
	fun toUserReviewEntity(): UserReviewEntity{
		return UserReviewEntity(
			id?:"",
			author?:"-",
			updatedAt?:"-",
			content?:"-",
			authorDetails?.avatarPath?:"",
			authorDetails?.rating?:0.0
		)
	}

}