package com.hafiztaruligani.movieday.domain.usecase

import androidx.paging.*
import com.hafiztaruligani.movieday.data.paging.UserReviewsRemoteMediator
import com.hafiztaruligani.movieday.domain.model.UserReview
import com.hafiztaruligani.movieday.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class GetUserReviewsPaged @Inject constructor(
    val movieRepository: MovieRepository
){
    suspend operator fun invoke(movieId: Int): Flow<PagingData<UserReview>>{

        val remoteMediator = UserReviewsRemoteMediator(movieId, movieRepository)
        return flow {

            Pager(
                config = PagingConfig(20),
                remoteMediator = remoteMediator
            ){
                movieRepository.getUserReviewsPaged()
            }.flow.collect { data ->
                emit(
                    data.map {
                        it.toUserReview()
                    }
                )
            }

        }
    }
}
