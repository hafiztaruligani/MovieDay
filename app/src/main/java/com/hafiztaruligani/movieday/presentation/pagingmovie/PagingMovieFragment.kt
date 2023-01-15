package com.hafiztaruligani.movieday.presentation.pagingmovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.hafiztaruligani.movieday.databinding.FragmentPagingMovieBinding
import com.hafiztaruligani.movieday.domain.model.MovieSimple
import com.hafiztaruligani.movieday.presentation.adapters.MoviePagingAdapter
import com.hafiztaruligani.movieday.presentation.adapters.PagingLoadStateAdapter
import com.hafiztaruligani.movieday.util.MovieSimpleDiffUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class PagingMovieFragment() : Fragment() {

    private val  viewModel by viewModels<PagingMovieViewModel>()
    private val args: PagingMovieFragmentArgs by navArgs()
    private lateinit var binding: FragmentPagingMovieBinding

    private lateinit var adapter: MoviePagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initiateData(args.genre)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()
        binding = FragmentPagingMovieBinding.inflate(layoutInflater)

        setupPager()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // submitting paging data
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                state.data?.let {
                    adapter.submitData(it)
                }
            }
        }
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun setupPager(){
        binding.apply {

            val diffUtil = MovieSimpleDiffUtil()
            adapter = MoviePagingAdapter(diffUtil, ::navigateToDetail)
            val loadStateAdapter = PagingLoadStateAdapter(adapter::retry)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            rcMovies.layoutManager = layoutManager
            (rcMovies.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false // disabling dim animation when update item
            rcMovies.adapter = adapter.withLoadStateFooter(loadStateAdapter)

            retry.setOnClickListener {
                adapter.retry()
            }

            /*
            when we add load state footer, there is a bug where sometimes the RC is scrolling to bottom automatically when initiating the data,
            this "initiate" variable is to avoid this bug, forcing the layout manager scrolling to top with specific condition
            */
            var initiate = false
            adapter.addOnPagesUpdatedListener {
                if(initiate){
                    layoutManager.scrollToPosition(0) // forcing the layout manager
                    initiate = false
                }
            }

            lifecycleScope.launchWhenResumed {
                adapter.loadStateFlow.collect(){ loadState ->

                    //if mediator "REFRESH" is loading, then this is initiate state
                    if(loadState.mediator?.refresh !is LoadState.Loading)
                        loadStateAdapter.loadState = loadState.mediator?.append ?: loadState.append
                    else initiate = true // set initiate into true and the layout manager scrolling to the top

                    // if there is no connection on initiate
                    val error = loadState.mediator?.refresh is LoadState.Error
                    rcMovies.isVisible = !error
                    message.isVisible = error
                    retry.isVisible = error

                }
            }

        }
    }

    private fun navigateToDetail(view: View, data: MovieSimple){
        // initiate transition object
        val transitionName = data.id.toString() + UUID.randomUUID()
        val extras = FragmentNavigatorExtras(view to transitionName)

        // navigation direction / action
        val direction = PagingMovieFragmentDirections.actionPagingMovieFragmentToDetailFragment(data, transitionName)
        findNavController().navigate(
            directions = direction,
            navigatorExtras = extras
        )
    }

}