package com.hafiztaruligani.movieday.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideosResponse(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("results")
	val videos: List<VideoItemResponse?>? = null
)