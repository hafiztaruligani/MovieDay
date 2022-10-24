package com.hafiztaruligani.movieday.domain.usecase

import androidx.paging.*
import com.hafiztaruligani.movieday.HttpExceptionBuilder
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.UserReviewEntity
import com.hafiztaruligani.movieday.data.paging.UserReviewsRemoteMediator
import com.hafiztaruligani.movieday.data.remote.dto.userreviews.UserReviewItemResponse
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
import java.util.*

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
@Extensions(value = [
    ExtendWith(MockitoExtension::class)
])
internal class GetUserReviewsPagedTest{

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var getUserReviewsPaged: GetUserReviewsPaged

    private val movieId = 1
    val listOfUserReviewResponse = mutableListOf<UserReviewItemResponse>()


    @BeforeEach
    fun setup(){
        getUserReviewsPaged = GetUserReviewsPaged(movieRepository)

        // fill list response
        repeat(20){
            listOfUserReviewResponse.add(
                UserReviewItemResponse(id = UUID.randomUUID().toString())
            )
        }
    }

    @Test
    fun `success (endOfPaginationReached = true), stop load data `() = runTest{

        Mockito.`when`(movieRepository.getUserReviewsRemote(movieId,1)).then { listOf<UserReviewItemResponse>() }

        val pagingState = PagingState<Int, UserReviewEntity>(
            listOf(),
            null,
            PagingConfig(20),
            10
        )
        val remoteMediator = UserReviewsRemoteMediator(movieId, movieRepository)
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue{ result is RemoteMediator.MediatorResult.Success }
        assertTrue { (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached }

    }

    @Test
    fun `success (endOfPaginationReached = false), need to load data again `() = runTest{

        Mockito.`when`(movieRepository.getUserReviewsRemote(movieId,1)).then { listOfUserReviewResponse }

        val pagingState = PagingState<Int, UserReviewEntity>(
            listOf(),
            null,
            PagingConfig(20),
            10
        )
        val remoteMediator = UserReviewsRemoteMediator(movieId, movieRepository)
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue { result is RemoteMediator.MediatorResult.Success }
        assertFalse { (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached }

    }

    @Test
    fun `failed (http exception)`() = runTest{

        // http exception
        Mockito.`when`(movieRepository.getUserReviewsRemote(movieId,2)).then { throw HttpExceptionBuilder.NotFoundException().exception }
        checkErrorResult()

        // network unavailable
        Mockito.`when`(movieRepository.getUserReviewsRemote(movieId,1)).then { throw IOException() }
        checkErrorResult()

    }

    private fun checkErrorResult() = runTest{

        val pagingState = PagingState<Int, UserReviewEntity>(
            listOf(),
            null,
            PagingConfig(20),
            10
        )

        val remoteMediator = UserReviewsRemoteMediator(movieId, movieRepository)
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue { result is RemoteMediator.MediatorResult.Error }

    }



}