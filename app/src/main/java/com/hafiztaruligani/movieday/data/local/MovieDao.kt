package com.hafiztaruligani.movieday.data.local

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.hafiztaruligani.movieday.data.local.entity.*
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.MoviePagingEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.RemoteKeyEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.UserReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query ("SELECT * FROM movie_genre ORDER BY name ")
    fun getMovieGenres(): Flow<List<MovieGenreEntity>>

    @Transaction
    @Query ("SELECT * FROM movie_genre ORDER BY name")
    fun getGenreWithMovies(): Flow<List<GenreWithMoviesEntity>>


    @Insert (onConflict = REPLACE)
    suspend fun insertMovies(list: List<MovieSimpleEntity>)

    @Insert (onConflict = REPLACE)
    suspend fun insertGenreMovieCrossRef(list: List<GenreMovieCrossRef>)

    @Insert (onConflict = REPLACE)
    suspend fun insertGenres(list: List<MovieGenreEntity>)

    @Query ("DELETE from movies_simple")
    suspend fun deleteMoviesSimple()

    @Query ("DELETE FROM genre_movie_cross_ref")
    suspend fun deleteGenreMovieCrossRef()

    // movie detail
    @Query("SELECT * FROM movie_detail WHERE id=:id")
    fun getMovieDetail(id: Int): Flow<MovieDetailEntity>

    @Insert (onConflict = REPLACE)
    suspend fun insertMovieDetail(movie: MovieDetailEntity)

    // movies paging
    @Insert (onConflict = REPLACE)
    suspend fun insertMoviesPaging(list: List<MoviePagingEntity>)

    @Query ("SELECT * FROM movies_paging ORDER BY releaseDate DESC")
    fun getMoviesPaged(): PagingSource<Int, MoviePagingEntity>

    @Query ("DELETE FROM movies_paging")
    suspend fun deleteMoviesPaging()

    // user reviews paging
    @Insert (onConflict = REPLACE)
    suspend fun insertUserReviews(data: List<UserReviewEntity>)

    @Query ("SELECT * FROM user_reviews ORDER BY updatedAt DESC")
    fun getUserReviewsPaged(): PagingSource<Int, UserReviewEntity>

    @Query ("DELETE FROM user_reviews")
    suspend fun deleteUserReviewsPaging()

    // remote key paging
    @Insert (onConflict = REPLACE)
    suspend fun insertMoviesRemoteKey(key: RemoteKeyEntity)

    @Query ("SELECT * FROM remote_key  WHERE id=:id")
    suspend fun getRemoteKey(id: String): RemoteKeyEntity

    @Query ("DELETE FROM remote_key WHERE id=:id")
    suspend fun deleteRemoteKey(id: String)

}