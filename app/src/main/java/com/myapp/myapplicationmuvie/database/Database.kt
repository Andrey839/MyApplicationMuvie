package com.myapp.myapplicationmuvie.database

import androidx.room.*
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoContainer( var container: List<Films>)

@Entity(tableName = "Films")
@JsonClass(generateAdapter = true)
data class Films(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "year")
    val year: Int,
    @ColumnInfo(name = "rating")
    @TypeConverters(ConverterRating::class)
    val ratings: List<String> = listOf(),
    @ColumnInfo(name = "name")
    val name: String
)

@JsonClass(generateAdapter = true)
@Entity(tableName = "Poster", foreignKeys = [ForeignKey(entity = Films::class, parentColumns = ["id"], childColumns = ["idFilmPoster"])],
indices = [Index("idFilmPoster")])
data class Poster(
    @PrimaryKey
    @ColumnInfo(name = "idFilmPoster")
    val idFilmPoster: Int,
    @ColumnInfo(name = "poster")
    val poster: String
)

@JsonClass(generateAdapter = true)
@Entity(tableName = "Trailer", foreignKeys = [ForeignKey(entity = Films::class, parentColumns = ["id"], childColumns = ["idFilmTrailer"])],
indices = [Index("idFilmTrailer")])
data class Trailer(
    @PrimaryKey
    @ColumnInfo(name = "idFilmTrailer")
    val idFilmTrailer: Int,
    @ColumnInfo(name = "trailer")
    val trailer: String
)

data class FilmWithPoster(
    @Embedded
    val film: Films,
    @Relation(
        parentColumn = "id",
        entityColumn = "idFilmPoster",
        entity = Poster::class
    )
    val poster: Poster
)

data class FilmWithTrailer(
    @Embedded
    val film: Films,
    @Relation(
        parentColumn = "id",
        entityColumn = "idFilmTrailer",
        entity = Trailer::class
    )
    val trailer: Trailer
)

data class FilmsId(
    val id: Int
)

