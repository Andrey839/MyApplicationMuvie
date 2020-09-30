package com.myapp.myapplicationmuvie.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoFilms {

    @Query("select * from Films")
    fun getVideo(): LiveData<List<Films>>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = Films::class)
    fun insertIdDescriptionYearName(vararg films: Films)

}

@Dao
interface DaoFavorite {

    @Insert
    @Transaction
    fun insertFavoritesFilm(film: Favorite)

    @Query("select * from FavoritesFilm where id =:id")
    fun queryOneFilm(id: Int): LiveData<Favorite>

    @Query("select * from FavoritesFilm")
    fun queryFilms(): LiveData<List<Favorite>>

    @Delete
    fun deleteOneFilm(film: Favorite)
}

@Dao
interface DaoModel {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModelFilm(film: Model)

    @Query("select * from AllInOne")
    fun queryAllFilms(): LiveData<List<Model>>
}

@Database(entities = [Films::class, Favorite::class, Model::class], version = 1)
@TypeConverters(ConverterRating::class, ConverterList::class)
abstract class DatabaseVideo : RoomDatabase() {
    abstract val daoFilms: DaoFilms
    abstract val daoFavorite: DaoFavorite
    abstract val daoModel: DaoModel
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