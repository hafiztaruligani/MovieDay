package com.hafiztaruligani.movieday.presentation.movies

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.movieday.domain.model.GenreWithMovies
import com.hafiztaruligani.movieday.domain.usecase.GetGenresWithMovies
import com.hafiztaruligani.movieday.presentation.CommonUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getGenresWithMovies: GetGenresWithMovies
) : ViewModel() {
    private val _uiState = MutableStateFlow( CommonUiState< List<GenreWithMovies> >(loading = true) )
    val uiState : StateFlow< CommonUiState< List< GenreWithMovies > > > = _uiState

    val scrollState = MutableStateFlow(mutableMapOf<Int, Parcelable?>())

    init {
        initiateData()
    }

    fun initiateData() = viewModelScope.launch(Dispatchers.IO) {

        getGenresWithMovies.invoke().collect(){
            when(it){
                is Resource.Success -> {
                    _uiState.value = CommonUiState(data = it.data)
                }
                is Resource.Error -> {
                    _uiState.value = CommonUiState(error = it.message, data = it.data?: listOf())
                }
                is Resource.Loading -> {
                    _uiState.value = CommonUiState(loading = true)
                }
            }
        }

    }

}