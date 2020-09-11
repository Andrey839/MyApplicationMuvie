package com.myapp.myapplicationmuvie.networkService

import com.myapp.myapplicationmuvie.database.Films
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmsJson(
    var id: Int,
    var description: String,
    var year: String,
    var ratings: List<Rating>,
    var name: String
)

data class Rating(
    val source: String,
    val rate: String
)

@JsonClass(generateAdapter = true)
data class PosterJson(
    val poster: String
)

@JsonClass(generateAdapter = true)
data class TrailerJson(
    val trailer: List<String>
)

fun List<FilmsJson>.asDatabase(): Array<Films>{
    return map {
            Films(
                id = it.id,
            description = it.description,
            year = it.year,
            ratings = it.ratings,
            name = it.name)
    }
        .toTypedArray()
}