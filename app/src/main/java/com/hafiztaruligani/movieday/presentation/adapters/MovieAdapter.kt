package com.hafiztaruligani.movieday.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.hafiztaruligani.movieday.databinding.ItemMovieBinding
import com.hafiztaruligani.movieday.domain.model.Genre
import com.hafiztaruligani.movieday.domain.model.MovieSimple
import com.hafiztaruligani.movieday.util.MovieSimpleDiffUtil
import com.hafiztaruligani.movieday.util.glide
import com.hafiztaruligani.movieday.util.removeBracket
import kotlinx.coroutines.launch

class MovieAdapter(
    private val detail: (View, MovieSimple) -> Unit,
    private val paging: (Genre) -> Unit,
    private val parentScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private val differ = MovieSimpleDiffUtil()
    private val asyncList = AsyncListDiffer(this, differ)
    private lateinit var genre: Genre
    private val fakeId = -2

    fun submitData(list: List<MovieSimple>, genre: Genre){

        if(list.isNotEmpty()){

            // add fake item (id=-2) in last for "See all" button
            val fakeItem = MovieSimple(fakeId,"","",0.0,"", listOf())
            val data = list.toMutableList()
            data.add(fakeItem)

            asyncList.submitList(data)
            this.genre = genre
        }

    }

    inner class ViewHolder(val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = asyncList.currentList[position]
            setupTransition(holder.binding, data)

            holder.binding.apply {

                seeAll.isVisible = data.id == fakeId
                arrow.isVisible = data.id == fakeId
                title.text = data.title

                voteAverage.text = if (data.voteAverage>0.0) data.voteAverage.toString() else "-"
                year.text = if(data.releaseDate.length>4) data.releaseDate.takeLast(4) else "-"

                poster.glide(data.poster)

                genres.text = data.genresName.removeBracket()

                root.setOnClickListener {
                    if(data.id==fakeId) paging.invoke(genre)
                    else detail.invoke(poster, data)

                }
        }
    }

    private fun setupTransition(binding: ItemMovieBinding, data: MovieSimple) {
        ViewCompat.setTransitionName(binding.poster, data.id.toString()+genre.name)
    }

    override fun getItemCount(): Int {
        return asyncList.currentList.size
    }
}