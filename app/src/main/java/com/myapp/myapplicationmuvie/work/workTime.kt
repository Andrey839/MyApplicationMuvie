package com.myapp.myapplicationmuvie.work

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.myapp.myapplicationmuvie.database.getDatabase
import com.myapp.myapplicationmuvie.repository.Repository

class WorkTime( context: Context, work: WorkerParameters): CoroutineWorker(appContext = context, params = work) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {

        val database = getDatabase(applicationContext)

        val repository = Repository(database)

        return try {
            repository.getNetworkFilms()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}