package com.hafiztaruligani.movieday.data.repository

import androidx.paging.PagingSource
import com.hafiztaruligani.movieday.data.local.MovieDao
import com.hafiztaruligani.movieday.data.local.entity.GenreWithMoviesEntity
import com.hafiztaruligani.movieday.data.local.entity.MovieDetailEntity
import com.hafiztaruligani.movieday.data.local.entity.MovieGenreEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.MoviePagingEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.RemoteKeyEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.UserReviewEntity
import com.hafiztaruligani.movieday.data.remote.ApiService
import com.hafiztaruligani.movieday.data.remote.dto.GenreResponse
import com.hafiztaruligani.movieday.data.remote.dto.VideoItemResponse
import com.hafiztaruligani.movieday.data.remote.dto.movie.MovieSimpleResponse
import com.hafiztaruligani.movieday.data.remote.dto.userreviews.UserReviewItemResponse
import com.hafiztaruligani.movieday.domain.model.Genre
import com.hafiztaruligani.movieday.domain.repository.MovieRepository
import com.hafiztaruligani.movieday.util.convertIntoList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MovieRepositoryImpl(
    private val apiService: ApiService,
    private val movieDao: MovieDao
): MovieRepository {

    override suspend fun getGenreWithMovies(): Flow<List<GenreWithMoviesEntity>>{
        val genreData = getGenreRemote().map { it.toGenreEntity() } // Get Genres From Remote

        CoroutineScope(Dispatchers.IO).launch {

            // INSERT genres data into database
            launch {
                movieDao.insertGenres(
                    genreData
                )
            }

            //DELETE old data
            movieDao.deleteMoviesSimple()
            movieDao.deleteGenreMovieCrossRef()

            // get movies from remote for each genre
            genreData.forEach { genre ->
                launch {

                    val movieData: List<MovieSimpleResponse> =
                        getMoviesByGenreRemote(genre.genreId, 1)

                    // INSERT movies data into database
                    movieDao.insertMovies(
                        movieData.map {
                            it.toMovieSimpleEntity(
                                genreData
                            )
                        }
                    )

                    // INSERT genre and movie cross ref (because M to M)
                    movieData.forEach { movieSimpleResponse ->
                        movieDao.insertGenreMovieCrossRef(
                            movieSimpleResponse.toGenreMovieCrossRef()
                        )
                    }
                }
            }
        }

        // return all genre with the movies
        return movieDao.getGenreWithMovies()
    }

    override suspend fun getMoviesByGenreRemote(genreId: Int, page: Int): List<MovieSimpleResponse>{
        return apiService.getMoviesByGenre(genreId = genreId, page = page).movies
    }

    // if the network offline, use this function
    override suspend fun getGenreWithMoviesOffline(): Flow<List<GenreWithMoviesEntity>> {
        return movieDao.getGenreWithMovies()
    }

    override suspend fun getMovieGenresOffline(): Flow<List<MovieGenreEntity>>{
        return movieDao.getMovieGenres()
    }


    private suspend fun getGenreRemote(): List<GenreResponse>{
        return apiService.getGenreMovie().genres
    }

    // movie detail

    override suspend fun getMovieDetail(movieId: Int): Flow<MovieDetailEntity> {
        // movies detail need to be cached, because the server can return 304

        var videos = mutableListOf<String>()
        try {
            // get videos and filtering site==YouTube && official (if exist)
           getMovieVideos(movieId).filter { it.site == "YouTube" }.forEach {
               it.key?.let { key ->
                   videos.add(key)
               }
           }
        }catch (e:Exception){ }

        // if server return 304, take videos from cache (database)
        if(videos.isEmpty())
            movieDao.getMovieDetail(movieId).firstOrNull()?.videos
                ?.convertIntoList()?.toMutableList()?.let { videos = it }

        // get movie detail
        val movie = apiService.getMovieDetail(movieId).toMovieDetailEntity(videos.toList())

        movieDao.insertMovieDetail(movie)

        return movieDao.getMovieDetail(movieId)
    }

    override suspend fun getMovieVideos(movieId: Int): List<VideoItemResponse> {
        val data = mutableListOf<VideoItemResponse>()
        apiService.getMovieVideos(movieId).videos?.forEach {
            it?.let { data.add(it) }
        }
        return data
    }

    // movies paging
    override suspend fun insertMoviesPaging(list: List<MoviePagingEntity>) {
        movieDao.insertMoviesPaging(list)
    }

    override fun getMoviesPaged(genre: Genre): PagingSource<Int, MoviePagingEntity> {
        return movieDao.getMoviesPaged()
    }

    override suspend fun deleteMoviesPaging() {
        movieDao.deleteMoviesPaging()
    }

    // remote key paging
    override suspend fun deleteRemoteKey(id: String) {
        movieDao.deleteRemoteKey(id)
    }

    override suspend fun insertRemoteKey(key: RemoteKeyEntity) {
        movieDao.insertMoviesRemoteKey(key)
    }

    override suspend fun getRemoteKey(id: String): RemoteKeyEntity {
        return movieDao.getRemoteKey(id)
    }

    // user reviews paging
    override suspend fun getUserReviewsRemote(movieId: Int, page: Int): List<UserReviewItemResponse> {
        val data = mutableListOf<UserReviewItemResponse>()
        apiService.getUserReviews(movieId, page).userReviewItemResponses?.forEach {
            it?.let {
                if((it.id?.isNotBlank() == true) && (it.author?.isNotBlank() == true))
                    data.add(it)
            }
        }
        return data
    }

    override suspend fun insertUserReviews(data: List<UserReviewEntity>) {
        movieDao.insertUserReviews(data)
    }

    override fun getUserReviewsPaged(): PagingSource<Int, UserReviewEntity> {
        return movieDao.getUserReviewsPaged()
    }

    override suspend fun deleteUserReviewsPaging() {
        movieDao.deleteUserReviewsPaging()
    }


}











