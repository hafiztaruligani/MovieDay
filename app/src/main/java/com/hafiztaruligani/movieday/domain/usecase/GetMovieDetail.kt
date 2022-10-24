package com.hafiztaruligani.movieday.domain.usecase

import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.movieday.domain.model.MovieDetail
import com.hafiztaruligani.movieday.domain.repository.MovieRepository
import com.hafiztaruligani.movieday.util.StatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetMovieDetail @Inject constructor( private val movieRepository: MovieRepository) {

    suspend operator fun invoke(movieId : Int): Flow<Resource<MovieDetail>> = flow {

        emit(Resource.Loading())
        try {

            movieRepository.getMovieDetail(movieId).collect(){ data ->
                emit(
                    Resource.Success(data.toMovieDetail())
                )
            }

        }catch (e: HttpException){

            when(e.code()){
                StatusCode.NotFound.code -> emit(Resource.Error(Resource.DATA_NOT_FOUND))
                else -> {
                    emit(Resource.Error())
                }
            }

        }catch (e: IOException){
            emit(Resource.Error(Resource.NETWORK_UNAVAILABLE))
        }

    }

}