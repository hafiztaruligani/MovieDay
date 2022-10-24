package com.hafiztaruligani.movieday.util

import androidx.recyclerview.widget.DiffUtil
import com.hafiztaruligani.movieday.domain.model.GenreWithMovies

class GenreWithMoviesDiffUtil: DiffUtil.ItemCallback<GenreWithMovies>() {
    override fun areItemsTheSame(oldItem: GenreWithMovies, newItem: GenreWithMovies): Boolean {
        return oldItem.genre.id == newItem.genre.id
    }

    override fun areContentsTheSame(oldItem: GenreWithMovies, newItem: GenreWithMovies): Boolean {
        return oldItem.movies.size == newItem.movies.size
    }
}