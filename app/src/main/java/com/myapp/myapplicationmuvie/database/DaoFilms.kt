package com.myapp.myapplicationmuvie.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoFilms {

    @Query("select * from Films")
    fun getVideo(): LiveData<List<Films>>

    @Query("select id from Films")
    fun getId(): LiveData<List<FilmsId>>

    @Transaction
    @Query("select * from Films")
    fun getPosterVideo(): LiveData<List<FilmWithPoster>>

    @Transaction
    @Query("select * from Films")
    fun getTrailerVideo(): LiveData<List<FilmWithTrailer>>

    @Transaction
    @Query("select * from Films where id = :idTrailer")
    fun getOneTrailer(idTrailer: Int): LiveData<FilmWithTrailer>

    @Transaction
    @Query("select * from Films where id =:idPoster")
    fun getOnePoster(idPoster: Int): LiveData<FilmWithPoster>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIdDescriptionYearName(vararg: List<Films>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPoster( poster: Poster)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrailers( trailer: Trailer?)
}

@Database(entities = [Films::class, Poster::class, Trailer::class], version = 1)
@TypeConverters(ConverterRating::class)
abstract class DatabaseVideo : RoomDatabase() {
    abstract val daoFilms: DaoFilms
}

private lateinit var INSTANCE: DatabaseVideo

fun getDatabase(context: Context): DatabaseVideo {
    synchronized(DatabaseVideo::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                DatabaseVideo::class.java,
                "videos"
            ).build()
        }
    }
    return INSTANCE

}