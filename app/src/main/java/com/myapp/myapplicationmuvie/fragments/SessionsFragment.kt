package com.myapp.myapplicationmuvie.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myapp.myapplicationmuvie.modelViews.SessionsViewModel
import com.myapp.myapplicationmuvie.R

class SessionsFragment : Fragment() {

    private lateinit var viewModel: SessionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sessions_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SessionsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}