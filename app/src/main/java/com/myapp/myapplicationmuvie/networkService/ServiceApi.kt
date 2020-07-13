package com.myapp.myapplicationmuvie.networkService

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.myapp.myapplicationmuvie.database.Poster
import com.myapp.myapplicationmuvie.database.Trailer
import com.myapp.myapplicationmuvie.database.VideoContainer
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val basicUrl = "http://kinoinfo.ru/api/"

interface ServiceFilms {
    @GET("film/?format=json")
    fun getFilms(@Query("year") year: Int): Deferred<VideoContainer>

    @GET("film_posters/?format=json")
    fun getPosters(@Query("id") id: Int): Deferred<Poster>

    @GET("film_trailers/?format=json")
    fun getTrailers(@Query("id") id: Int): Deferred<Trailer>

 //   @GET("schedule/?format=json")
 //   fun getSessions(@Query("city") city: String): Deferred<List<Container.Sessions>>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Api {
    private val retrofitFilmsNow = Retrofit.Builder()

        .baseUrl(basicUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val getData = retrofitFilmsNow.create(ServiceFilms::class.java)!!
}