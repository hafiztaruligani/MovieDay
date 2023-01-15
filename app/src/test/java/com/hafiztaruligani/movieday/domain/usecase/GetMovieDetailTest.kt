package com.hafiztaruligani.movieday.domain.usecase

import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.movieday.ExpectedResult
import com.hafiztaruligani.movieday.HttpExceptionBuilder
import com.hafiztaruligani.movieday.data.local.entity.MovieDetailEntity
import com.hafiztaruligani.movieday.domain.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@Extensions(value = [
    ExtendWith(MockitoExtension::class)
])
class GetMovieDetailTest{

    @Mock
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMovieDetail: GetMovieDetail

    private val movieId = 1
    private val movieDetail = MovieDetailEntity(movieId,"","",2,"","","","",1.1,2)
    private val flowOfMovieDetail = flow { emit(movieDetail) }

    @BeforeEach
    fun setup(){
        getMovieDetail = GetMovieDetail(movieRepository)
    }

    @Test
    fun success()  = runTest{

        Mockito.`when`(movieRepository.getMovieDetail(movieId)).then { flow {
            delay(100)
            emit(movieDetail) }
        }

        val resultFlow = getMovieDetail.invoke(movieId)
        assert(resultFlow.count()==2)

        val resultClass = resultFlow.toList().map { it::class }

        assert(resultClass == ExpectedResult.SuccessResourceClass().result)

        //assert the data is correct
        assert((resultFlow.last() as Resource.Success).data.id == movieId)

    }

    @Test
    fun `failed (network exception)`() = runTest {

        Mockito.lenient().`when`(movieRepository.getMovieDetail(movieId)).then { throw IOException() }

        val resultFlow = getMovieDetail.invoke(movieId)
        assert(resultFlow.count()==2)

        val result = resultFlow.toList().map { it::class }

        assert(result == ExpectedResult.ErrorResourceClass().result)
        assert((resultFlow.last() as Resource.Error).message.isNotBlank())

    }
    @Test
    fun `failed (movie not found)`()= runTest {

        Mockito.lenient().`when`(movieRepository.getMovieDetail(movieId)).then { throw HttpExceptionBuilder.NotFoundException().exception }

        val resultFlow = getMovieDetail.invoke(movieId)

        val resultClass = resultFlow.toList().map { it::class }

        assert(resultClass == ExpectedResult.ErrorResourceClass().result)
        assert((resultFlow.last() as Resource.Error).message == Resource.DATA_NOT_FOUND)

    }


}