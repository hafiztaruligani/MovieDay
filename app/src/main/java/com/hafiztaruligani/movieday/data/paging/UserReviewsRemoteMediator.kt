package com.hafiztaruligani.movieday.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.RemoteKeyEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.UserReviewEntity
import com.hafiztaruligani.movieday.domain.repository.MovieRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class UserReviewsRemoteMediator(
    private val movieId: Int,
    private val movieRepository: MovieRepository
) : RemoteMediator<Int, UserReviewEntity>(){

    companion object{
        private const val REMOTE_KEY_ID = "user_reviews"
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserReviewEntity>
    ): MediatorResult {

        try {

            val page = when(loadType){
                REFRESH -> 1
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                APPEND -> {
                    movieRepository.getRemoteKey(REMOTE_KEY_ID).nextPage?:
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val data = movieRepository.getUserReviewsRemote(movieId, page).map { it.toUserReviewEntity() }
            movieRepository.insertUserReviews(data)
            movieRepository.insertRemoteKey( RemoteKeyEntity( id = REMOTE_KEY_ID, nextPage = page+1 ) )

            return MediatorResult.Success(endOfPaginationReached = data.isEmpty())

        } catch (e: HttpException){
            return MediatorResult.Error(e)
        } catch (e:IOException){
            return MediatorResult.Error(e)
        } catch (e: Exception){
            return MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        movieRepository.deleteUserReviewsPaging()
        movieRepository.deleteRemoteKey(REMOTE_KEY_ID)
        return super.initialize()
    }
}
