package com.myapp.myapplicationmuvie.modelViews

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.myapp.myapplicationmuvie.R
import com.myapp.myapplicationmuvie.database.DatabaseVideo
import com.myapp.myapplicationmuvie.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
class RegistrationViewModel(database: DatabaseVideo, private val auth: FirebaseAuth, val context: Activity) : ViewModel() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    var email: String? = null
    var password: String? = null

    private val currentUser = auth.currentUser

    private var _successOrNotInput = MutableLiveData<Boolean>()

    init {
        if (currentUser == null) {
            scope.launch {
                Repository(database).getNetworkFilms()
            }
        }
    }

    val successOrNotInput: LiveData<Boolean>
    get() = _successOrNotInput

    fun createUserWithEmailAndPassword() {
        email?.let {
            auth.createUserWithEmailAndPassword(it, password.toString())
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        Toast.makeText(context,context.getString(R.string.registration_succesful), Toast.LENGTH_LONG).show()
                        _successOrNotInput.value = true

  // update UI
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(context, context.getString(R.string.registration_error), Toast.LENGTH_SHORT).show()
                        //           updateUI(null)
                        _successOrNotInput.value = false
                    }
                }
        }
    }

    fun signInWithEmailAndPassword() {
        email?.let {
            auth.signInWithEmailAndPassword(it, password.toString())
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        // update UI
                        _successOrNotInput.value = true
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(context, context.getString(R.string.registration_error), Toast.LENGTH_SHORT).show()
                        _successOrNotInput.value = false
                    }
                }
        }
    }
}

class RegistrationViewModelFactory(
    private val dataSource: DatabaseVideo,
    private val auth: FirebaseAuth,
    private val context: Activity
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel( dataSource, auth, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}