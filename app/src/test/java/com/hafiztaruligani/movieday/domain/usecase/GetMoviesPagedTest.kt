package com.hafiztaruligani.movieday.domain.usecase

import androidx.paging.*
import com.hafiztaruligani.movieday.HttpExceptionBuilder
import com.hafiztaruligani.movieday.data.local.entity.MovieGenreEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.MoviePagingEntity
import com.hafiztaruligani.movieday.data.paging.MoviesPagingRemoteMediator
import com.hafiztaruligani.movieday.data.remote.dto.movie.MovieSimpleResponse
import com.hafiztaruligani.movieday.domain.model.Genre
import com.hafiztaruligani.movieday.domain.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.io.IOException

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
@Extensions(value = [
    ExtendWith(MockitoExtension::class)
])
internal class GetMoviesPagedTest {

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var getMoviesPaged: GetMoviesPaged

    private val mockData = mutableListOf<MovieSimpleResponse>()

    @BeforeEach
    fun setup(){

        getMoviesPaged = GetMoviesPaged(movieRepository)

        // fill list response
        repeat(20){
            mockData.add(
                MovieSimpleResponse(id = it, genreIds = listOf(1,2,3))
            )
        }

    }

    @Test
    fun `success (endOfPaginationReached = true), stop load data `() = runTest{

        Mockito.`when`(movieRepository.getMoviesByGenreRemote(1,1)).then { listOf<MovieSimpleResponse>() }

        val pagingState = PagingState<Int, MoviePagingEntity>(
            listOf(),
            null,
            PagingConfig(20),
            10
        )
        val remoteMediator = MoviesPagingRemoteMediator(Genre(1,""), listOf(), movieRepository)
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue{ result is RemoteMediator.MediatorResult.Success }
        assertTrue { (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached }

    }

    @Test
    fun `success (endOfPaginationReached = false), need to load data again `() = runTest{

        Mockito.`when`(movieRepository.getMoviesByGenreRemote(1,1)).then { mockData }

        val pagingState = PagingState<Int, MoviePagingEntity>(
            listOf(),
            null,
            PagingConfig(20),
            10
        )
        val remoteMediator = MoviesPagingRemoteMediator(Genre(1,""), listOf(), movieRepository)
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue { result is RemoteMediator.MediatorResult.Success }
        assertFalse { (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached }

    }

    @Test
    fun `failed (http exception)`() = runTest{

        // http exception
        Mockito.`when`(movieRepository.getMoviesByGenreRemote(1,2)).then { throw HttpExceptionBuilder.NotFoundException().exception }
        checkErrorResult()

        // network unavailable
        Mockito.`when`(movieRepository.getMoviesByGenreRemote(1,1)).then { throw IOException("asd") }
        checkErrorResult()

    }

    private fun checkErrorResult() = runTest{

        val pagingState = PagingState<Int, MoviePagingEntity>(
            listOf(),
            null,
            PagingConfig(20),
            10
        )
        val remoteMediator = MoviesPagingRemoteMediator(Genre(1,"asd"), listOf(MovieGenreEntity(1,"")), movieRepository)
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue { result is RemoteMediator.MediatorResult.Error }

    }



}
