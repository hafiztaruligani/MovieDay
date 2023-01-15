package com.hafiztaruligani.movieday.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hafiztaruligani.movieday.databinding.ItemVideosBinding
import com.hafiztaruligani.movieday.util.glide

class VideosAdapter(private val thumbnailUrl: String, val playVideo: (String) -> Unit) : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemVideosBinding): RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
    val asyncList = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( ItemVideosBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = asyncList.currentList[position]
        val thumbnail = thumbnailUrl.replace("KEY", key)
        holder.binding.apply {

            thumbnailImage.glide(
                resource = thumbnail
            )
            root.setOnClickListener {
                playVideo.invoke(key)
            }
        }

    }

    override fun getItemCount() = asyncList.currentList.size

}
