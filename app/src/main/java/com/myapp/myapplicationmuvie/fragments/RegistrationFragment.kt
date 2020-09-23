package com.myapp.myapplicationmuvie.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.myapp.myapplicationmuvie.R
import com.myapp.myapplicationmuvie.database.getDatabase
import com.myapp.myapplicationmuvie.databinding.RegistrationFragmentBinding
import com.myapp.myapplicationmuvie.modelViews.RegistrationViewModel
import com.myapp.myapplicationmuvie.modelViews.RegistrationViewModelFactory
import kotlin.system.exitProcess

class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private var auth: FirebaseAuth? = null

    private val viewModel: RegistrationViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val database = getDatabase(application)
        auth = FirebaseAuth.getInstance()
        val context = requireNotNull(this.activity)
        ViewModelProvider(this, RegistrationViewModelFactory(database, auth!!, context)).get(
            RegistrationViewModel::class.java
        )
    }

    @SuppressLint("CommitPrefEdits")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: RegistrationFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.registration_fragment, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    view?.let {
                        Snackbar.make(it, getString(R.string.exit_app), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.OK)) {
                                exitProcess(1)
                            }.show()
                    }
                }
            })

        binding.inputName.addTextChangedListener {
            val preferenceName = activity?.getPreferences(Context.MODE_PRIVATE)
            preferenceName?.edit() ?: return@addTextChangedListener with(preferenceName?.edit()) {
                this?.putString("name", it.toString())
                this?.apply()
            }
        }

        binding.inputEmail.addTextChangedListener {
            if (it.toString() == "") binding.inputEmailLayout.error = getString(R.string.error)
            else {
                viewModel.email = it.toString()
                binding.inputEmailLayout.error = null
            }
        }

        binding.inputPassword.addTextChangedListener {
            if (it.toString() == "") binding.inputPasswordLayout.error = getString(R.string.error)
            else {
                if (it.toString().length < 8) binding.inputPasswordLayout.error =
                    getString(R.string.errorPassword)
                else {
                    viewModel.password = it.toString()
                    binding.inputPasswordLayout.error = null
                    binding.inputAgainPasswordLayout.isEnabled = true
                    binding.buttonSignIn.isEnabled = true
                }
            }
        }

        binding.inputAgainPassword.addTextChangedListener {
            if (it.toString() == "") binding.inputAgainPasswordLayout.error =
                getString(R.string.error)
            else {
                if (viewModel.password != it.toString()) binding.inputAgainPasswordLayout.error =
                    getString(R.string.errorPasswordAgain)
                else {
                    binding.inputAgainPasswordLayout.error = null
                    binding.buttonRegistration.isEnabled = true
                }
            }
        }

        viewModel.successOrNotInput.observe(viewLifecycleOwner, Observer {
            if (it) {
                toHomeFragment()
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth?.currentUser

        if (currentUser != null) {
            toHomeFragment()
        }
    }

    private fun toHomeFragment() {
        if (findNavController().currentDestination?.id == R.id.registrationFragment) {
            this.findNavController()
                .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment())
        }
    }

}