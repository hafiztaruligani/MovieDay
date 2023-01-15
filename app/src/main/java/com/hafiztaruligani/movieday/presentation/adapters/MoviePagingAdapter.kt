package com.hafiztaruligani.movieday.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hafiztaruligani.movieday.R
import com.hafiztaruligani.movieday.databinding.ItemMoviePagingBinding
import com.hafiztaruligani.movieday.domain.model.MovieSimple
import com.hafiztaruligani.movieday.util.MovieSimpleDiffUtil
import com.hafiztaruligani.movieday.util.addChip
import com.hafiztaruligani.movieday.util.glide


class MoviePagingAdapter (
    private val movieDiffUtil: MovieSimpleDiffUtil,
    private val detail : (View, MovieSimple) -> Unit
)
    : PagingDataAdapter<MovieSimple, MoviePagingAdapter.ViewHolder>(movieDiffUtil){

    inner class ViewHolder(val binding: ItemMoviePagingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.apply {
            data?.let {

                ViewCompat.setTransitionName(poster, data.id.toString())

                poster.glide(data.poster)
                title.text = data.title
                year.text = data.releaseDate
                voteAverage.text = data.voteAverage.toString()

                genres.removeAllViews()
                data.genresName.forEachIndexed{ n, str->
                    genres.addChip(n*(position+1), str, R.color.primary)
                }

                root.setOnClickListener {
                    detail.invoke(poster, data)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMoviePagingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}