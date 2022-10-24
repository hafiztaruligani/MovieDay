package com.hafiztaruligani.movieday.data.remote

import com.hafiztaruligani.movieday.BuildConfig
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfigTest {
    fun provideApi(): ApiService {

        /*val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient().newBuilder()
            .addNetworkInterceptor(logger)
            .build()*/

        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.SERVER_URL)
           // .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}