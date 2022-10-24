package com.hafiztaruligani.movieday.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hafiztaruligani.movieday.data.local.entity.GenreMovieCrossRef
import com.hafiztaruligani.movieday.data.local.entity.MovieDetailEntity
import com.hafiztaruligani.movieday.data.local.entity.MovieGenreEntity
import com.hafiztaruligani.movieday.data.local.entity.MovieSimpleEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.MoviePagingEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.RemoteKeyEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.UserReviewEntity

@Database(
    entities = [
        MovieGenreEntity::class,
        GenreMovieCrossRef::class,
        MovieSimpleEntity::class,
        MoviePagingEntity::class,
        RemoteKeyEntity::class,
        UserReviewEntity::class,
        MovieDetailEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object{
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "movieday").build()
    }
}