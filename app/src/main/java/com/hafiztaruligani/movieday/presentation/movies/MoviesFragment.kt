package com.hafiztaruligani.movieday.presentation.movies

import android.os.Bundle
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
import com.hafiztaruligani.movieday.R
import com.hafiztaruligani.movieday.databinding.FragmentMoviesBinding
import com.hafiztaruligani.movieday.domain.model.Genre
import com.hafiztaruligani.movieday.domain.model.MovieSimple
import com.hafiztaruligani.movieday.presentation.adapters.MovieGenresAdapter
import com.hafiztaruligani.movieday.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel by viewModels<MoviesViewModel>()
    private lateinit var binding: FragmentMoviesBinding

    private lateinit var adapter: MovieGenresAdapter
    private var initiateView = true


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

                    // error handling
                    if(state.error.isNotBlank() && (state.data?.isEmpty() == true || state.data == null )) {
                        errorMessage.isVisible = true
                        retry.isVisible = true
                        cover.isVisible = true
                    }

                    // loading state view. if loading finished the show the action bar
                    if (!state.loading && adapter.itemCount==0) setupActionBar()
                    loading.isVisible = state.loading
                    logo.isVisible = state.loading && initiateView

                    if (state.loading) initiateView = false

                }

                if (state.data?.isNotEmpty() == true) {
                    //submit the data into adapter
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

        // saving scroll position
        viewModel.scrollState.value = adapter.scrollStates
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // restore scroll position
        adapter.scrollStates = viewModel.scrollState.value

        // create action bar
        //setupActionBar()
    }

    private fun setupActionBar(){
        (activity as MainActivity).setupActionBar(requireContext().resources.getString(R.string.app_name))
    }

}




















