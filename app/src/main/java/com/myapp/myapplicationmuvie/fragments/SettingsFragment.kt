package com.myapp.myapplicationmuvie.fragments

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.auth.FirebaseAuth
import com.myapp.myapplicationmuvie.R

const val KEY_RENAME = "rename"
private const val KEY_AVATAR = "avatar"
const val KEY_URI = "uri"
private const val KEY_DELETE_ACCOUNT = "delete"
private const val GIVE_IMAGE = "image/*"

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
        val namePreference = findPreference<EditTextPreference>(KEY_RENAME)
        val editor = sharedPreferences?.edit()
        val photoPreference = findPreference<Preference>(KEY_AVATAR)

        val getUri = registerForActivityResult(ActivityResultContracts.GetContent()) {
            editor?.putString(KEY_URI, it.toString())?.apply()
        }

        photoPreference?.setOnPreferenceClickListener {
            getUri.launch(GIVE_IMAGE)
            true
        }

        namePreference?.setOnPreferenceChangeListener { preference, newValue ->
            editor?.putString(KEY_NAME, newValue.toString())?.apply()
            true
        }

        val simplePreference = findPreference<Preference>(KEY_DELETE_ACCOUNT)?.apply {
            summary = FirebaseAuth.getInstance().currentUser?.email
        }

        simplePreference?.setOnPreferenceClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this.context, getString(R.string.exit_done), Toast.LENGTH_LONG).show()
            if (findNavController().currentDestination?.id == R.id.settings_item) {
                this.findNavController()
                    .navigate(SettingsFragmentDirections.actionSettingsItemToRegistrationFragment())
            }
            return@setOnPreferenceClickListener true
        }
    }
}