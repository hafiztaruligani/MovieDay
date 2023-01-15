package com.hafiztaruligani.movieday.domain.usecase

import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.movieday.domain.model.GenreWithMovies
import com.hafiztaruligani.movieday.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetGenresWithMovies @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<GenreWithMovies>>>{
        return flow {

            // fill in resource status as LOADING
            emit(Resource.Loading())

            suspend fun offlineData() = movieRepository.getGenreWithMoviesOffline().collect() { data ->

                if (data.isNotEmpty())
                    emit(
                        Resource.Error(data = data.map { it.toGenreWithMovies() })
                    )
                else emit( Resource.Error() )
            }

            try {
                movieRepository.getGenreWithMovies().collect(){ data ->

                    /*
                        if the data empty (database not filled, still waiting for remote),
                        the Resource status still loading.
                        if the data is ready, then fill in resource status as SUCCESS
                    */
                    if(data.isNotEmpty())
                        emit(
                            Resource.Success(
                                data.map {
                                    it.toGenreWithMovies()
                                }
                            )
                        )
                }

            } catch (e: HttpException){
                // catch api failure/error and fill in resource status as ERROR
                // and give offline data (if exists)
                offlineData()
            } catch (e: IOException){
                offlineData()
            }

        }
    }


}