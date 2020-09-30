package com.myapp.myapplicationmuvie.modelViews

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapp.myapplicationmuvie.database.Favorite
import com.myapp.myapplicationmuvie.repository.Repository

class FavoritesViewModel(private val repository: Repository) : ViewModel() {

    val listFavorites: LiveData<List<Favorite>>
    get() = repository.listFavoriteFilms
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