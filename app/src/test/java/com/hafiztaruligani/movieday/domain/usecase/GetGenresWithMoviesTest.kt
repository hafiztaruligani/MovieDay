package com.hafiztaruligani.movieday.domain.usecase

import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.movieday.ExpectedResult
import com.hafiztaruligani.movieday.HttpExceptionBuilder
import com.hafiztaruligani.movieday.data.local.entity.GenreWithMoviesEntity
import com.hafiztaruligani.movieday.data.local.entity.MovieGenreEntity
import com.hafiztaruligani.movieday.data.local.entity.MovieSimpleEntity
import com.hafiztaruligani.movieday.domain.model.GenreWithMovies
import com.hafiztaruligani.movieday.domain.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@Extensions(value = [
    ExtendWith(MockitoExtension::class)
])
internal class GetGenresWithMoviesTest{

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var getGenresWithMovies: GetGenresWithMovies

    private val genresWithMovies = mutableListOf<GenreWithMoviesEntity>()

    @BeforeEach
    fun setup(){
        getGenresWithMovies = GetGenresWithMovies(movieRepository)
        // genres
        repeat(5) { i ->
            var movieList  = mutableListOf<MovieSimpleEntity>()

            //movies
            repeat(10) { j ->

                movieList.add(
                    MovieSimpleEntity(
                        Random().nextInt(),
                        "",
                        "2022-10-$j",
                        1.0,
                        "",
                        listOf("genre_name1, genre_name2").toString()
                    )
                )

            }

            movieList = movieList.sortedByDescending { it.releaseDate }.toMutableList()

            genresWithMovies.add(
                GenreWithMoviesEntity(
                    genre = MovieGenreEntity(i, UUID.randomUUID().toString()),
                    movies =  movieList.toList()
                )
            )

        }
    }

    @Test
    fun success() = runTest{
        Mockito.`when`(movieRepository.getGenreWithMovies()).then { flow { emit(genresWithMovies) } }

        val resultFlow = getGenresWithMovies.invoke()

        assert(resultFlow.count() == 2)

        val resultClass = resultFlow.toList().map { it::class }

        // assert returned class of item in flow is correct and sequentially
        assert(
            resultClass == ExpectedResult.SuccessResourceClass().result
        )

        // assert last item is success
        assert(resultFlow.last() is Resource.Success)


        val data = (resultFlow.last() as Resource.Success<List<GenreWithMovies>>).data
        checkData(data)

    }

    // assert returned data is correct
    private fun checkData(data: List<GenreWithMovies>) {
        assert(
            data.map { it.genre.id } == genresWithMovies.map { it.genre.genreId }
        )

        assert(
            data.map { it.movies.map { it.id } } == genresWithMovies.map { it.movies.map { it.movieId } }
        )
    }

    @Test
    fun `failed (data not found)`() = runTest{
        Mockito.`when`(movieRepository.getGenreWithMovies()).then { throw HttpExceptionBuilder.NotFoundException().exception }
        Mockito.`when`(movieRepository.getGenreWithMoviesOffline()).then { flow { emit(genresWithMovies) } }

        val resultFlow = getGenresWithMovies.invoke()
        println(resultFlow.count())
        assert(resultFlow.count() == 2)

        val resultClass = resultFlow.toList().map { it::class }

        // assert returned class of item in flow is correct and sequentially
        assert(
            resultClass == ExpectedResult.ErrorResourceClass().result
        )

        // assert last item is error
        assert(resultFlow.last() is Resource.Error)

        // assert still getting correct data from database
        val data = (resultFlow.last() as Resource.Error).data
        assert(data !=null)
        checkData( data!! )
    }

    @Test
    fun `failed (network unavailable)`() = runTest{
        Mockito.`when`(movieRepository.getGenreWithMovies()).then { throw IOException("tmp") }
        Mockito.`when`(movieRepository.getGenreWithMoviesOffline()).then { flow { emit(genresWithMovies) } }

        val resultFlow = getGenresWithMovies.invoke()

        assert(resultFlow.count() == 2)

        val resultClass = resultFlow.toList().map { it::class }

        // assert returned class of item in flow is correct and sequentially
        assert(
            resultClass == ExpectedResult.ErrorResourceClass().result
        )

        // assert last item is error
        assert(resultFlow.last() is Resource.Error)

        // assert still getting correct data from database
        val data = (resultFlow.last() as Resource.Error).data
        assert(data !=null)
        checkData( data!! )
    }

}

















