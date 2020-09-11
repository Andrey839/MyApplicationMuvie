package com.myapp.myapplicationmuvie.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.preference.*
import com.google.firebase.auth.FirebaseAuth
import com.myapp.myapplicationmuvie.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
        val namePreference = findPreference<EditTextPreference>("rename")
        val editor = sharedPreferences?.edit()
        val photoPreference = findPreference<Preference>("avatar")

        val getUri = registerForActivityResult(ActivityResultContracts.GetContent()){
            editor?.putString("uri", it.toString())?.apply()
            Log.e("tyi", "$it")
        }

        photoPreference?.setOnPreferenceClickListener {
            getUri.launch("image/*")
            true
        }

        namePreference?.setOnPreferenceChangeListener { preference, newValue ->
            editor?.putString("name", newValue.toString())?.apply()
            true
        }

        val simplePreference = findPreference<Preference>("delete")?.apply {
            summary = FirebaseAuth.getInstance().currentUser?.email
        }

        simplePreference?.setOnPreferenceClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this.context, "Выполнен выход из аккаунта", Toast.LENGTH_LONG).show()
            if (findNavController().currentDestination?.id == R.id.settings_item) {
                this.findNavController().navigate(SettingsFragmentDirections.actionSettingsItemToRegistrationFragment())
            }
            return@setOnPreferenceClickListener true
        }
    }
}