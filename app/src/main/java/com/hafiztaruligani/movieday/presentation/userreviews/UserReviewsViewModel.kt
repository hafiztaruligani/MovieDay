package com.hafiztaruligani.movieday.presentation.userreviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hafiztaruligani.movieday.domain.model.UserReview
import com.hafiztaruligani.movieday.domain.usecase.GetUserReviewsPaged
import com.hafiztaruligani.movieday.presentation.CommonUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserReviewsViewModel @Inject constructor(
    private val getUserReviewsPaged : GetUserReviewsPaged
): ViewModel() {

    private val _uiState = MutableStateFlow( CommonUiState< PagingData<UserReview> >() )
    val uiState : StateFlow<CommonUiState < PagingData<UserReview> > > = _uiState

    fun initiateData(movieId: Int) = viewModelScope.launch (Dispatchers.IO) {
        getUserReviewsPaged.invoke(movieId).cachedIn(viewModelScope).collectLatest {
            _uiState.emit(
                CommonUiState(
                    data = it
                )
            )
        }
    }

}