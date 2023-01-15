package com.hafiztaruligani.movieday.data.remote

import com.hafiztaruligani.movieday.BuildConfig
import com.hafiztaruligani.movieday.data.remote.dto.GenresResponse
import com.hafiztaruligani.movieday.data.remote.dto.VideosResponse
import com.hafiztaruligani.movieday.data.remote.dto.movie.MovieDiscoveryResponse
import com.hafiztaruligani.movieday.data.remote.dto.movie.detail.MovieDetailResponse
import com.hafiztaruligani.movieday.data.remote.dto.userreviews.UserReviewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.*

interface ApiService {

    companion object{
        private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        private val currentDate =  sdf.format(Date())
    }

    @GET("genre/movie/list")
    suspend fun getGenreMovie(): GenresResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1,
        @Query("primary_release_date.lte") releaseDate: String = currentDate,
        @Query("sort_by") sortBy: String = "release_date.desc",
        @Query("vote_count.gte") voteCountMinimum : Int = 25
    ): MovieDiscoveryResponse

    @GET("movie/{id}/reviews")
    suspend fun getUserReviews(
        @Path("id") movieId: Int,
        @Query("page") page: Int
    ): UserReviewsResponse

    @GET("movie/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id") movieId: Int
    ): VideosResponse

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") movieId: Int
    ): MovieDetailResponse

}