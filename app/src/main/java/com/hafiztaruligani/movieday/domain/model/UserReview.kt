package com.hafiztaruligani.movieday.domain.model

data class UserReview(
    val id: String,
    val author: String,
    val updatedAt: String,
    val content: String,
    val avatar: String,
    val rating: Double
)
