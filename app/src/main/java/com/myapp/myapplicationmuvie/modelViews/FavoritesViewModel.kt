package com.myapp.myapplicationmuvie.modelViews

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapp.myapplicationmuvie.database.Favorite
import com.myapp.myapplicationmuvie.repository.Repository

class FavoritesViewModel(repository: Repository) : ViewModel() {

    private var _listFavorites = repository.listFavoriteFilms

    val listFavorites: LiveData<List<Favorite>>
    get() = _listFavorites
}

class FavoritesViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}