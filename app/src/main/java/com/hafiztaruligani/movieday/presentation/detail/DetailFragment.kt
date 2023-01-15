package com.hafiztaruligani.movieday.presentation.detail

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.hafiztaruligani.movieday.BuildConfig
import com.hafiztaruligani.movieday.R
import com.hafiztaruligani.movieday.databinding.FragmentDetailBinding
import com.hafiztaruligani.movieday.presentation.adapters.VideosAdapter
import com.hafiztaruligani.movieday.presentation.main.MainActivity
import com.hafiztaruligani.movieday.util.Cons.KEY
import com.hafiztaruligani.movieday.util.addChip
import com.hafiztaruligani.movieday.util.glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {

    val viewModel by viewModels<DetailViewModel>()
    lateinit var binding : FragmentDetailBinding
    private val args : DetailFragmentArgs by navArgs()
    private lateinit var videosAdapter: VideosAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initData(args.movieSimple.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailBinding.inflate(layoutInflater)
        setupTransition()
        setupVideos()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // submitting data into UI
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect() { state ->
                binding.apply {

                    loading.isVisible = state.loading

                    if(state.error.isNotBlank()) {
                        Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    else state.data?.let { data ->

                        releaseDate.text = data.releaseDate
                        runtime.text = data.runtime
                        overview.text = data.overview
                        voteAverage.text = data.voteAverage
                        voteCount.text = data.voteCount

                        insertGenres(genres, data.genresName)
                        videosAdapter.asyncList.submitList(data.videos)
                        unavailable.isVisible = data.videos.isEmpty()

                    }

                }
            }
        }

    }

    private fun setupVideos() {
        binding.apply {

            videosAdapter = VideosAdapter(BuildConfig.YOUTUBE_THUMBNAIL_URL, ::playVideo)
            videosViewPager.adapter = videosAdapter

            TabLayoutMediator( tabLayout, videosViewPager) { _, _ ->
            }.attach()

            viewPagerArrowRight.setOnClickListener {
                if(videosViewPager.currentItem<videosAdapter.itemCount)
                videosViewPager.currentItem = videosViewPager.currentItem+1
            }

            viewPagerArrowLeft.setOnClickListener {
                if(videosViewPager.currentItem>0)
                    videosViewPager.currentItem = videosViewPager.currentItem-1
            }

        }
    }

    private fun insertGenres(genres: ChipGroup, genresName: List<String>) {
        genres.removeAllViews()

       // val chip = layoutInflater.inflate(R.layout.item_genre, binding.root ) as Chip
        genresName.forEachIndexed { n, name->
            genres.addChip(n+1, name, R.color.secondary)
        }
    }

    private fun setupTransition() {
        // set transition destination
        ViewCompat.setTransitionName(binding.poster, args.transitionName)

        // set transition animation
        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        animation.duration = 500
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
        val data = args.movieSimple
        binding.apply {
            poster.glide(data.poster)
            title.text = data.title

            btnUserReviews.setOnClickListener {
                navigateToUserReviews()
            }

        }
    }

    private fun navigateToUserReviews() {
        val direction = DetailFragmentDirections.actionDetailFragmentToUserReviewsFragment(args.movieSimple)
        findNavController().navigate(
            directions = direction
        )
    }

    private fun playVideo(key: String){
        (activity as MainActivity).startActivity(
            Intent(requireContext(), YoutubePlayer::class.java)
            .putExtra(KEY, key)
        )
    }

}