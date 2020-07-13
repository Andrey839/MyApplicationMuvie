package com.myapp.myapplicationmuvie.modelViews

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapp.myapplicationmuvie.database.DatabaseVideo

class DetailedViewModel(private val id: Int, private val dataSource: DatabaseVideo) : ViewModel() {
    // TODO: Implement the ViewModel
}

class DetailedViewModelFactory(
    private val id: Int,
    private val dataSource: DatabaseVideo
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailedViewModel::class.java)) {
            return DetailedViewModel(id, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}