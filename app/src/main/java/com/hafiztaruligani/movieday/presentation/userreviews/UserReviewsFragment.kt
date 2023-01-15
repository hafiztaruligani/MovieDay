package com.hafiztaruligani.movieday.presentation.userreviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.hafiztaruligani.movieday.databinding.FragmentUserReviewsBinding
import com.hafiztaruligani.movieday.presentation.adapters.PagingLoadStateAdapter
import com.hafiztaruligani.movieday.presentation.adapters.UserReviewPagingAdapter
import com.hafiztaruligani.movieday.util.UserReviewDiffUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class UserReviewsFragment : Fragment() {

    private val viewModel by viewModels<UserReviewsViewModel>()
    private lateinit var binding: FragmentUserReviewsBinding
    private val args : UserReviewsFragmentArgs by navArgs()
    private lateinit var adapter : UserReviewPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initiateData(args.movieSimple.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserReviewsBinding.inflate(layoutInflater)

        setupPager()

        return binding.root
    }

    private fun setupPager() {
        val diffUtil = UserReviewDiffUtil()
        adapter = UserReviewPagingAdapter(diffUtil)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val loadStateAdapter = PagingLoadStateAdapter(adapter::retry)

        binding.apply {

            rcUserReviews.adapter = adapter.withLoadStateFooter(loadStateAdapter)
            rcUserReviews.layoutManager = layoutManager

            retry.setOnClickListener {
                adapter.retry()
            }

            /*
            when we add load state footer, there is a bug where sometimes the RC is scrolling to bottom automatically when initiating the data,
            this "initiate" variable is to avoid this bug, forcing the layout manager scrolling to top with specific condition
            */
            var initiate = false
            adapter.addOnPagesUpdatedListener {
                if(initiate)
                    layoutManager.scrollToPosition(0) // forcing the layout manager
            }

            // collecting load state flow
            lifecycleScope.launchWhenResumed {
                adapter.loadStateFlow.collect(){ loadState ->

                    //if mediator "REFRESH" is loading, then this is initiate state
                    if(loadState.mediator?.refresh !is LoadState.Loading) {
                        loadStateAdapter.loadState = loadState.mediator?.append?: loadState.append
                    } else initiate = true // set initiate into true and the layout manager scrolling to the top


                    // chek if there is no user reviews
                    val endPage = loadState.prepend.endOfPaginationReached
                                && loadState.refresh is LoadState.NotLoading
                                && loadState.append is LoadState.NotLoading
                                && loadState.prepend is LoadState.NotLoading
                    val pageEmpty = adapter.itemCount == 0
                    userReviewEmpty.isVisible = endPage && pageEmpty

                    // if there is no connection on initiate
                    val error = loadState.mediator?.refresh is LoadState.Error
                    rcUserReviews.isVisible = !error
                    message.isVisible = error
                    retry.isVisible = error

                }
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // submitting data
        lifecycleScope.launchWhenResumed {
            viewModel.uiState.collectLatest { state ->
                state.data?.let {
                    adapter.submitData(it)
                }
            }
        }

    }

}