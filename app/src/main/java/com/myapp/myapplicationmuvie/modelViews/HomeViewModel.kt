package com.myapp.myapplicationmuvie.modelViews

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.myapp.myapplicationmuvie.database.DatabaseVideo
import com.myapp.myapplicationmuvie.database.Model
import com.myapp.myapplicationmuvie.repository.Repository

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(val context: Activity, dataSource: DatabaseVideo) :
    AndroidViewModel(context.application) {

    private val repository = Repository(dataSource)

    private val _listModel = repository.listModelFilms

    val listModel: LiveData<List<Model>>
    get() = _listModel

}

class HomeVideoViewModelFactory(
    private val context: Activity,
    private val dataSource: DatabaseVideo
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(context, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
