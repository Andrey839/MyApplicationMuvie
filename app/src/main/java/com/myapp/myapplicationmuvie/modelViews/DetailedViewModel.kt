package com.myapp.myapplicationmuvie.modelViews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapp.myapplicationmuvie.database.Favorite
import com.myapp.myapplicationmuvie.database.Model
import com.myapp.myapplicationmuvie.database.toFavorite
import com.myapp.myapplicationmuvie.networkService.TrailerJson
import com.myapp.myapplicationmuvie.repository.Repository
import kotlinx.coroutines.*

class DetailedViewModel(
    private val id: Model,
    private val repository: Repository
) : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    var urlTrailer = MutableLiveData<List<TrailerJson>>()
    private var string: List<TrailerJson> = listOf()

    val model = MutableLiveData<Model>()

    var addOrDelete: Boolean? = null

    private val _snackBoolean = MutableLiveData<Boolean>()

    private var _favoritesValue: LiveData<Favorite> = repository.favoriteFilm(id.id)

    init {
        scope.launch {
            delay(10000)
            string = repository.getTrailerFilm(id.id)
            urlTrailer.value = string
        }
    }

    val favoritesValue: LiveData<Favorite>
        get() = _favoritesValue

    val snackBoolean: LiveData<Boolean>
    get() = _snackBoolean

    fun getModelId() {
        model.value = id
    }

    fun deleteFilmWithFavorite(){
        scope.launch {
            repository.deleteFavoriteFilm(id.toFavorite())
        }
    }

    fun clickStarsButton() {
        scope.launch {
            if (addOrDelete != null) {
                if (addOrDelete as Boolean) {
                    _snackBoolean.value = true
                } else {
                    repository.addFavoriteFilm(id.toFavorite())
                    _snackBoolean.value = false
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}

class DetailedViewModelFactory(
    private val id: Model,
    private val repository: Repository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailedViewModel::class.java)) {
            return DetailedViewModel(id, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}