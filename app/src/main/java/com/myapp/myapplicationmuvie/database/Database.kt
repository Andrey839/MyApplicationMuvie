package com.myapp.myapplicationmuvie.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.myapp.myapplicationmuvie.networkService.Rating
import kotlinx.android.parcel.Parcelize

// Entity table Films

@Entity(tableName = "Films")
data class Films(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "year")
    val year: String,
    @ColumnInfo(name = "rating")
    @TypeConverters(ConverterRating::class, ConverterList::class)
    var ratings: List<Rating> = listOf(),
    @ColumnInfo(name = "name")
    val name: String
)
// Entity table All in One Table

@Entity(tableName = "AllInOne")
@Parcelize
data class Model(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val description: String,
    val name: String,
    val year: String,
    val ratings: String,
    val poster: String
) : Parcelable
// Entity Favorite table

@Entity(tableName = "FavoritesFilm")
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val description: String,
    val year: String,
    @TypeConverters(ConverterRating::class, ConverterList::class)
    val ratings: String,
    val name: String,
    val poster: String
)

data class Rating(
    val source: String,
    val rate: String
)

fun Favorite.toModel(): Model {
    return Model(
        id,
        description,
        name,
        year,
        ratings,
        poster
    )
}

fun Model.toFavorite(): Favorite{
    return Favorite(
        id,
        description,
        year,
        ratings,
        name,
        poster
    )
}

