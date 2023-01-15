package com.hafiztaruligani.movieday.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.movieday.domain.model.MovieDetail
import com.hafiztaruligani.movieday.domain.usecase.GetMovieDetail
import com.hafiztaruligani.movieday.presentation.CommonUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetail: GetMovieDetail
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommonUiState<MovieDetail>())
    val uiState : StateFlow<CommonUiState<MovieDetail>> = _uiState

    fun initData(movieId: Int) = viewModelScope.launch (Dispatchers.IO){
        getMovieDetail.invoke(movieId).collectLatest { resource ->
            when (resource){
                is Resource.Error -> {
                    _uiState.value = CommonUiState(error = resource.message, data = resource.data)
                }
                is Resource.Loading -> _uiState.value = CommonUiState(loading = true)
                is Resource.Success -> _uiState.value = CommonUiState(data = resource.data)
            }
        }
    }

}