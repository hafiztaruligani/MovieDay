package com.hafiztaruligani.movieday.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.hafiztaruligani.movieday.data.local.entity.MovieGenreEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.MoviePagingEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.RemoteKeyEntity
import com.hafiztaruligani.movieday.domain.model.Genre
import com.hafiztaruligani.movieday.domain.repository.MovieRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MoviesPagingRemoteMediator (
    val genre: Genre,
    val genres: List<MovieGenreEntity>,
    val movieRepository: MovieRepository
): RemoteMediator<Int,MoviePagingEntity>() {

    companion object{
        private const val REMOTE_KEY_ID = "movies"
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MoviePagingEntity>
    ): MediatorResult {
        try {

            // initiate page
            val page: Int = when(loadType){
                REFRESH -> 1
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {
                    // take next page from database
                    movieRepository.getRemoteKey(REMOTE_KEY_ID).nextPage?:
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            // get data from remote by genre and page
            val data: List<MoviePagingEntity> = movieRepository.getMoviesByGenreRemote(genreId = genre.id, page).map { it.toMoviePagingEntity(genres) }

            // insert data from remote into database
            movieRepository.insertMoviesPaging(data)
            movieRepository.insertRemoteKey(RemoteKeyEntity(REMOTE_KEY_ID,page+1))

            return MediatorResult.Success(endOfPaginationReached = data.isEmpty())
        }catch (e: HttpException){
            return MediatorResult.Error(e)
        }catch (e: IOException){
            return MediatorResult.Error(e)
        }catch (e: Exception){
            return MediatorResult.Error(e)
        }

    }

    override suspend fun initialize(): InitializeAction {
        // delete data from database
        movieRepository.deleteMoviesPaging()
        movieRepository.deleteRemoteKey(REMOTE_KEY_ID)
        return super.initialize()
    }

}