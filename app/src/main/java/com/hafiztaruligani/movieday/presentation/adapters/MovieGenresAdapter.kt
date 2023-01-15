package com.hafiztaruligani.movieday.presentation.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hafiztaruligani.movieday.databinding.ItemListGenreBinding
import com.hafiztaruligani.movieday.domain.model.Genre
import com.hafiztaruligani.movieday.domain.model.MovieSimple
import com.hafiztaruligani.movieday.util.GenreWithMoviesDiffUtil

class MovieGenresAdapter(
    private val detail: (View, MovieSimple) -> Unit,
    private val paging: (Genre) -> Unit,
    private val parentScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<MovieGenresAdapter.ViewHolder>() {

    private val diffUtil = GenreWithMoviesDiffUtil()
    val asyncList = AsyncListDiffer(this, diffUtil)
    var scrollStates = mutableMapOf<Int, Parcelable?>()

    inner class ViewHolder(val binding: ItemListGenreBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = asyncList.currentList[position]
        val id = holder.layoutPosition+1 // for RC id and scrollStates id

        holder.binding.apply {

            val adapter = MovieAdapter(detail, paging, parentScope)
            val layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
            adapter.submitData(data.movies, data.genre)

            rcMovies.id = id
            genreName.text = data.genre.name
            rcMovies.layoutManager = layoutManager
            rcMovies.adapter = adapter

            // restore child scroll state
            setScrollStatesListener(rcMovies, id)

            btnSeeAll.setOnClickListener {
                paging.invoke(data.genre)
            }

        }
    }

    private fun setScrollStatesListener(rcMovies: RecyclerView, id: Int) {
        // restore
        val state = scrollStates[id]
        if (state != null) {
            rcMovies.layoutManager?.onRestoreInstanceState(state)
        }

        // save
        rcMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                scrollStates[recyclerView.id] =
                    recyclerView.layoutManager?.onSaveInstanceState()
            }
        })
    }

    override fun getItemCount(): Int {
        return asyncList.currentList.size
    }
}