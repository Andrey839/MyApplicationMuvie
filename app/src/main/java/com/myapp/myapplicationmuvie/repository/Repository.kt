package com.myapp.myapplicationmuvie.repository

 import android.os.Build
 import androidx.annotation.RequiresApi
 import androidx.lifecycle.LiveData
 import com.myapp.myapplicationmuvie.database.DatabaseVideo
 import com.myapp.myapplicationmuvie.database.Favorite
 import com.myapp.myapplicationmuvie.database.Model
 import com.myapp.myapplicationmuvie.database.filmsPosterToModel
 import com.myapp.myapplicationmuvie.modelViews.DELAY
 import com.myapp.myapplicationmuvie.networkService.Api
 import com.myapp.myapplicationmuvie.networkService.FilmsJson
 import com.myapp.myapplicationmuvie.networkService.TrailerJson
 import com.myapp.myapplicationmuvie.networkService.asDatabase
 import com.squareup.moshi.JsonDataException
 import kotlinx.coroutines.Dispatchers
 import kotlinx.coroutines.delay
 import kotlinx.coroutines.withContext
 import java.util.*

class Repository(private val databaseVideo: DatabaseVideo) {

    private val data = Calendar.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    private val currentData = data.get(Calendar.YEAR)

    val listModelFilms: LiveData<List<Model>> = databaseVideo.daoModel.queryAllFilms()
    val listFavoriteFilms: LiveData<List<Favorite>> = databaseVideo.daoFavorite.queryFilms()

    fun favoriteFilm(id: Int): LiveData<Favorite> {
           return databaseVideo.daoFavorite.queryOneFilm(id)
    }

    suspend fun getTrailerFilm(id: Int): List<TrailerJson>{
        return withContext(Dispatchers.IO) {
            try {
                return@withContext Api.getData.getTrailersAsync(id).await()
            } catch (e: Exception) {
                return@withContext listOf(TrailerJson(listOf(""+e)))
            }
        }
    }
// add favorite film in database
    suspend fun addFavoriteFilm(favoriteFilm: Favorite){
        withContext(Dispatchers.IO) {
            databaseVideo.daoFavorite.insertFavoritesFilm(favoriteFilm)
        }
    }

    suspend fun deleteFavoriteFilm(favoriteFilm: Favorite){
        withContext(Dispatchers.IO) {
            databaseVideo.daoFavorite.deleteOneFilm(favoriteFilm)
        }
    }

// request network
        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun getNetworkFilms() {
            withContext(Dispatchers.IO) {
                var playlist: List<FilmsJson> = listOf()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        playlist = Api.getData.getFilmsAsync(currentData).await()
                    } catch (e: Exception) { }
                } else {
                    try {
                        val year = 2020
                       playlist = Api.getData.getFilmsAsync(year = year).await()
                    } catch (e: Exception) {}
                }
// add information about films in Database
                databaseVideo.daoFilms.insertIdDescriptionYearName(*playlist.asDatabase())
                for (i in  playlist) {
                    delay(DELAY)
                    addPoster(i)
                }
            }
        }


    // query poster in network and add to Model
    private suspend fun addPoster(film: FilmsJson){
        withContext(Dispatchers.IO) {
            try {
                val poster = Api.getData.getPostersAsync(film.id).await()
                databaseVideo.daoModel.insertModelFilm(filmsPosterToModel(film, poster[0].poster))
            }catch (i: JsonDataException) {
            }
        }
    }
}