package com.myapp.myapplicationmuvie.repository

 import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
 import com.myapp.myapplicationmuvie.database.*
 import com.myapp.myapplicationmuvie.networkService.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class Repository(private val databaseVideo: DatabaseVideo) {

    private val data = Calendar.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    private val currentData = data.get(Calendar.YEAR)

    val listFilm: LiveData<List<Films>> = databaseVideo.daoFilms.getVideo()
    val listPoster: LiveData<List<FilmWithPoster>> = databaseVideo.daoFilms.getPosterVideo()

    fun getOneTrailer(id: Int): LiveData<FilmWithTrailer>{
        return databaseVideo.daoFilms.getOneTrailer(id)
    }

    fun getOnePoster(id: Int): LiveData<FilmWithPoster>{
        return databaseVideo.daoFilms.getOnePoster(id)
    }

// request network
    init {
        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun getNetworkFilms() {
            withContext(Dispatchers.IO) {
                val playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Api.getData.getFilms(currentData).await()
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
// add information about films in Database
                databaseVideo.daoFilms.insertIdDescriptionYearName(playlist.container)
                val id = databaseVideo.daoFilms.getId()
                for (i in 0..id.value!!.size){
                    val poster = Api.getData.getPosters(i).await()
                    val trailer = Api.getData.getTrailers(i).await()
// add in Database posters and trailers
                    databaseVideo.daoFilms.insertPoster(poster)
                    databaseVideo.daoFilms.insertTrailers(trailer)
                }

            }
        }
    }
}