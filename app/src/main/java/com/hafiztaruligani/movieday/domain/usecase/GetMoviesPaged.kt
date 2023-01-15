package com.hafiztaruligani.movieday.domain.usecase

import androidx.paging.*
import com.hafiztaruligani.movieday.data.paging.MoviesPagingRemoteMediator
import com.hafiztaruligani.movieday.domain.model.Genre
import com.hafiztaruligani.movieday.domain.model.MovieSimple
import com.hafiztaruligani.movieday.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class GetMoviesPaged @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(genre: Genre): Flow<PagingData<MovieSimple>>{
        val genres = movieRepository.getMovieGenresOffline().first()
        val remoteMediator = MoviesPagingRemoteMediator(genre, genres, movieRepository)
        return flow {
            Pager(
                config = PagingConfig(20),
                remoteMediator = remoteMediator
            ){
                movieRepository.getMoviesPaged(genre)
            }.flow.collect { data ->
                emit(
                    data.map { it.toMovieSimple() }
                )
            }
        }
    }

}