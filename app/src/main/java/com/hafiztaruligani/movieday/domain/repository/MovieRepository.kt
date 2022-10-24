package com.hafiztaruligani.movieday.domain.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.hafiztaruligani.movieday.data.local.entity.GenreWithMoviesEntity
import com.hafiztaruligani.movieday.data.local.entity.MovieDetailEntity
import com.hafiztaruligani.movieday.data.local.entity.MovieGenreEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.MoviePagingEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.RemoteKeyEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.UserReviewEntity
import com.hafiztaruligani.movieday.data.remote.dto.VideoItemResponse

import com.hafiztaruligani.movieday.data.remote.dto.movie.MovieSimpleResponse
import com.hafiztaruligani.movieday.data.remote.dto.movie.detail.MovieDetailResponse
import com.hafiztaruligani.movieday.data.remote.dto.userreviews.UserReviewItemResponse
import com.hafiztaruligani.movieday.data.remote.dto.userreviews.UserReviewsResponse
import com.hafiztaruligani.movieday.domain.model.Genre
import com.hafiztaruligani.movieday.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getGenreWithMovies(): Flow<List<GenreWithMoviesEntity>>
    suspend fun getGenreWithMoviesOffline(): Flow<List<GenreWithMoviesEntity>>
    suspend fun getMovieGenresOffline(): Flow<List<MovieGenreEntity>>

    // movies paging
    suspend fun getMoviesByGenreRemote(genreId: Int, page: Int): List<MovieSimpleResponse>
    suspend fun insertMoviesPaging(list: List<MoviePagingEntity>)
    fun getMoviesPaged(genre: Genre): PagingSource<Int, MoviePagingEntity>
    suspend fun deleteMoviesPaging()

    // user reviews paging
    suspend fun getUserReviewsRemote(movieId: Int, page: Int): List<UserReviewItemResponse>
    suspend fun insertUserReviews(data: List<UserReviewEntity>)
    fun getUserReviewsPaged(): PagingSource<Int, UserReviewEntity>
    suspend fun deleteUserReviewsPaging()

    // remote key
    suspend fun deleteRemoteKey(id: String)
    suspend fun getRemoteKey(id: String): RemoteKeyEntity
    suspend fun insertRemoteKey(key: RemoteKeyEntity)

    // movie detail
    suspend fun getMovieVideos(movieId: Int): List<VideoItemResponse>
    suspend fun getMovieDetail(movieId: Int): Flow<MovieDetailEntity>


}