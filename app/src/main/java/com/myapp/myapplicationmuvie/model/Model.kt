package com.myapp.myapplicationmuvie.model

import com.myapp.myapplicationmuvie.networkService.FilmsJson

object toModel {

    fun filmsPosterToModel(film: FilmsJson, poster: String): com.myapp.myapplicationmuvie.database.Model {
        return com.myapp.myapplicationmuvie.database.Model(
            id = film.id,
            description = film.description,
            name = film.name,
            year = film.year,
            ratings = film.ratings[0].rate,
            poster = poster
        )
    }
}
