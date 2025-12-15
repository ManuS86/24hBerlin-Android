package com.example.a24hberlin.data.api

import com.example.a24hberlin.BuildConfig
import com.example.a24hberlin.data.model.ServerResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://www.twenty-four-hours.info/"

private val logger: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }
}

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(logger)
    .build()

private val moshi = Moshi
    .Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit
    .Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(httpClient)
    .build()

interface EventApiService {
    @GET("/wp-json/eventon/events?post_status=publish")
    suspend fun getEvents(): ServerResponse
}

object EventApi {
    val retrofitService: EventApiService by lazy { retrofit.create(EventApiService::class.java) }
}