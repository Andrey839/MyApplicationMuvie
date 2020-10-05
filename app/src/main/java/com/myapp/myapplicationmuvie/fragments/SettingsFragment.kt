package com.myapp.myapplicationmuvie.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.myapp.myapplicationmuvie.R
import kotlinx.android.synthetic.main.activity_main.*

const val KEY_RENAME = "rename"
private const val KEY_AVATAR = "avatar"
const val KEY_URI = "uri"
private const val KEY_DELETE_ACCOUNT = "delete"
private const val GIVE_IMAGE = "image/*"

class SettingsFragment : PreferenceFragmentCompat() {

    private var visiblePreferencePhoto: Boolean = true

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    view?.context,
                    getString(R.string.permission_check),
                    Toast.LENGTH_LONG
                ).show()
                visiblePreferencePhoto = true
            } else visiblePreferencePhoto = false

        }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
        val namePreference = findPreference<EditTextPreference>(KEY_RENAME)
        val editor = sharedPreferences?.edit()
        val photoPreference = findPreference<Preference>(KEY_AVATAR)

        val getUri = registerForActivityResult(ActivityResultContracts.GetContent()) {
            editor?.putString(KEY_URI, it.toString())?.apply()
            Toast.makeText(context, getString(R.string.avatar_check), Toast.LENGTH_LONG).show()
        }

        photoPreference?.setOnPreferenceClickListener {
            if (!visiblePreferencePhoto) Toast.makeText(
                context,
                getString(R.string.permission_no_check),
                Toast.LENGTH_LONG
            ).show()
            when {
                (context?.let { it1 ->
                    ContextCompat.checkSelfPermission(
                        it1, Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
                        == PackageManager.PERMISSION_GRANTED) -> {

                    getUri.launch(GIVE_IMAGE)
                }
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    view?.let { it1 ->
                        Snackbar.make(it1, getString(R.string.picture_avatar), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.OK)) {
                                requestPermissionLauncher.launch(
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                            }
                    }
                }
                else -> {
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            }
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