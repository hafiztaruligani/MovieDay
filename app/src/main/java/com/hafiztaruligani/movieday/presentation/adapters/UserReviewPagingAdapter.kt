package com.hafiztaruligani.movieday.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hafiztaruligani.movieday.BuildConfig
import com.hafiztaruligani.movieday.R
import com.hafiztaruligani.movieday.databinding.ItemUserReviewBinding
import com.hafiztaruligani.movieday.domain.model.UserReview
import com.hafiztaruligani.movieday.util.UserReviewDiffUtil
import com.hafiztaruligani.movieday.util.glide
import java.text.SimpleDateFormat
import java.util.*

class UserReviewPagingAdapter(diffUtil: UserReviewDiffUtil): PagingDataAdapter<UserReview, UserReviewPagingAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding : ItemUserReviewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = getItem(position)

        holder.binding.apply {
            data?.let {

                author.text = data.author
                rating.text = data.rating.toString().plus("/10")
                val a = data.updatedAt
                date.text = a
                content.text = data.content

                if(data.avatar.contains("http"))
                    avatar.glide(resource = data.avatar.drop(1), circleCrop = true, placeholder = R.drawable.ic_baseline_person_24)
                else
                    avatar.glide(resource = BuildConfig.IMAGE_URL.plus(data.avatar), circleCrop = true, placeholder = R.drawable.ic_baseline_person_24)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( ItemUserReviewBinding.inflate( LayoutInflater.from(parent.context), parent, false ) )
    }

}
