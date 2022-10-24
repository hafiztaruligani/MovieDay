package com.hafiztaruligani.movieday.util

import androidx.recyclerview.widget.DiffUtil
import com.hafiztaruligani.movieday.domain.model.MovieSimple

class MovieSimpleDiffUtil: DiffUtil.ItemCallback<MovieSimple>() {
    override fun areItemsTheSame(oldItem: MovieSimple, newItem: MovieSimple): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieSimple, newItem: MovieSimple): Boolean {
        return oldItem.title == newItem.title
    }
}