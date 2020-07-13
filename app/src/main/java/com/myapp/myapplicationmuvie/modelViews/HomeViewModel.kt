package com.myapp.myapplicationmuvie.modelViews

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapp.myapplicationmuvie.database.DaoFilms
import com.myapp.myapplicationmuvie.database.DatabaseVideo
import com.myapp.myapplicationmuvie.database.getDatabase
import com.myapp.myapplicationmuvie.repository.Repository

class HomeViewModel(val context: Activity, private val dataSource: DatabaseVideo) : ViewModel() {

    init {
        fun getRepository(){
            _repository.value = listOf(Repository(dataSource))
        }
    }

    private val _repository = MutableLiveData<List<Repository>>()

    val repository: LiveData<List<Repository>>
    get() = _repository

    private val _toDetailedInfoFilm = MutableLiveData<Int>()

    val toDetailedInfoFilm: LiveData<Int>
    get() = _toDetailedInfoFilm

    fun  detailedInfoFilm(id: Int){
        _toDetailedInfoFilm.value = id
    }

    fun detailedInfoFilmReset(){
        _toDetailedInfoFilm.value = null
    }


}

class HomeVideoViewModelFactory(
    private val context: Activity,
    private val dataSource: DatabaseVideo) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(context, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
