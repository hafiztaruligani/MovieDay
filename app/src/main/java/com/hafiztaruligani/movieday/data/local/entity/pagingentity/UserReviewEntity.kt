package com.hafiztaruligani.movieday.data.local.entity.pagingentity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.hafiztaruligani.movieday.domain.model.UserReview
import com.hafiztaruligani.movieday.util.toStringDate

@Entity (tableName = "user_reviews")
data class UserReviewEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val author: String,
    val updatedAt: String,
    val content: String,
    val avatar: String,
    val rating: Double
){
    @Ignore
    fun toUserReview() = UserReview(
        id = id,
        author = author,
        updatedAt = updatedAt.toStringDate(),
        content = content,
        avatar = avatar,
        rating = rating
    )
}
