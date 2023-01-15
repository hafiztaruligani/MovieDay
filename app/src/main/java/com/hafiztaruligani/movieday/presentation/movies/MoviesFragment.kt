package com.hafiztaruligani.movieday.presentation.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.hafiztaruligani.movieday.R
import com.hafiztaruligani.movieday.databinding.FragmentMoviesBinding
import com.hafiztaruligani.movieday.domain.model.Genre
import com.hafiztaruligani.movieday.domain.model.MovieSimple
import com.hafiztaruligani.movieday.presentation.adapters.MovieGenresAdapter
import com.hafiztaruligani.movieday.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.log


@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel by viewModels<MoviesViewModel>()
    private lateinit var binding: FragmentMoviesBinding

    private lateinit var adapter: MovieGenresAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()
        binding = FragmentMoviesBinding.inflate(layoutInflater)

        setupRC()

        return binding.root
    }

    private fun setupRC() {
        binding.apply {

            val layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            adapter = MovieGenresAdapter(::navigateToDetail, ::navigateToPaging, lifecycleScope)
            adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            rcGenres.adapter = adapter
            rcGenres.layoutManager = layoutManager
            (rcGenres.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            retry.setOnClickListener {
                viewModel.initiateData()
                retry.isVisible = false
                errorMessage.isVisible = false
                cover.isVisible = false
            }

        }
    }

    private fun navigateToDetail(view: View, data: MovieSimple){
        // initiate transition object
        val transitionName = data.id.toString() + UUID.randomUUID()
        val extras = FragmentNavigatorExtras(view to transitionName)

        // navigation direction / action
        val direction = MoviesFragmentDirections.actionMoviesFragmentToDetailFragment(data, transitionName)
        findNavController().navigate(
            directions = direction,
            navigatorExtras = extras
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun bindData() {
        var n = 1
        lifecycleScope.launch {
            viewModel.uiState.collect() { state ->

                binding.apply {

                    if(state.error.isNotBlank() && (state.data?.isEmpty() == true || state.data == null )) {
                        errorMessage.isVisible = true
                        retry.isVisible = true
                        cover.isVisible = true
                    }
                    if(!state.loading||state.error.isNotBlank()) dataLoaded()
                }

                if (state.data?.isNotEmpty() == true) {
                    adapter.asyncList.submitList(state.data)
                    n++
                }
                
            }
        }
    }

    private fun navigateToPaging(genre: Genre){
        val direction = MoviesFragmentDirections.actionMoviesFragmentToPagingMovieFragment(genre)
        findNavController().navigate(
            directions = direction
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.scrollState.value = adapter.scrollStates
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        adapter.scrollStates = viewModel.scrollState.value
    }

    private fun dataLoaded(){
        (activity as MainActivity).dismissSplashScreen()
    }

}




















