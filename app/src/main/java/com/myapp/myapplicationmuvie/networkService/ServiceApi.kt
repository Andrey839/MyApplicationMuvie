package com.myapp.myapplicationmuvie.networkService

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val basicUrl = "http://kinoinfo.ru/api/"
private const val JSON = "?format=json"

interface ServiceFilms {
    @GET("film/?format=json")
    fun getFilmsAsync(@Query("year") year: Int): Deferred<List<FilmsJson>>

    @GET("film_posters/$JSON")
    fun getPostersAsync(@Query("id") id: Int): Deferred<List<PosterJson>>

    @GET("film_trailers/$JSON")
    fun getTrailersAsync(@Query("id") id: Int): Deferred<List<TrailerJson>>

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

    val getData: ServiceFilms = retrofitFilmsNow.create(ServiceFilms::class.java)
}