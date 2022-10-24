package com.hafiztaruligani.movieday.di

import android.content.Context
import com.hafiztaruligani.movieday.BuildConfig
import com.hafiztaruligani.movieday.data.local.AppDatabase
import com.hafiztaruligani.movieday.data.local.MovieDao
import com.hafiztaruligani.movieday.data.remote.ApiService
import com.hafiztaruligani.movieday.data.repository.MovieRepositoryImpl
import com.hafiztaruligani.movieday.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideApi(): ApiService {

        val authenticator = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var default = chain.request()
                val token = BuildConfig.API_KEY
                val url = default.url.newBuilder().addQueryParameter("api_key", token).build()
                default = default.newBuilder().url(url).build()
                return chain.proceed(default)
            }
        }

        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient().newBuilder()
            .addNetworkInterceptor(logger)
            .addInterceptor(authenticator)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase{
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao{
        return appDatabase.movieDao()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(movieDao: MovieDao, apiService: ApiService): MovieRepository{
        return MovieRepositoryImpl(
            apiService = apiService,
            movieDao = movieDao
        )
    }
}