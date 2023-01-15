package com.hafiztaruligani.movieday.presentation.pagingmovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hafiztaruligani.movieday.domain.model.Genre
import com.hafiztaruligani.movieday.domain.model.MovieSimple
import com.hafiztaruligani.movieday.domain.usecase.GetMoviesPaged
import com.hafiztaruligani.movieday.presentation.CommonUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagingMovieViewModel @Inject constructor(
    private val getMoviesPaged: GetMoviesPaged
) : ViewModel() {

    private val _uiState = MutableStateFlow( CommonUiState< PagingData<MovieSimple> >() )
    val uiState : StateFlow< CommonUiState< PagingData<MovieSimple> > > = _uiState

    fun initiateData(genre: Genre) = viewModelScope.launch (Dispatchers.IO) {
        getMoviesPaged.invoke(genre).cachedIn(viewModelScope).collectLatest {
            _uiState.emit(CommonUiState(data = it))
        }
    }

}