package com.hafiztaruligani.movieday.util

import androidx.recyclerview.widget.DiffUtil
import com.hafiztaruligani.movieday.domain.model.UserReview

class UserReviewDiffUtil : DiffUtil.ItemCallback<UserReview>() {

    override fun areItemsTheSame(oldItem: UserReview, newItem: UserReview): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserReview, newItem: UserReview): Boolean {
        return oldItem.content == newItem.content
    }

}
