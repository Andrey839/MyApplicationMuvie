package com.myapp.myapplicationmuvie.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.myapp.myapplicationmuvie.modelViews.DetailedViewModel
import com.myapp.myapplicationmuvie.R
import com.myapp.myapplicationmuvie.database.getDatabase
import com.myapp.myapplicationmuvie.databinding.DetailedFragmentBinding
import com.myapp.myapplicationmuvie.modelViews.DetailedViewModelFactory

class DetailedFragment : Fragment() {

    companion object {
        fun newInstance() = DetailedFragment()
    }

    private lateinit var viewModel: DetailedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DetailedFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.detailed_fragment,
            container, false)

        val application = requireNotNull(this.activity).application
        val database = getDatabase(application)
        val argument = arguments?.let { DetailedFragmentArgs.fromBundle(it) }

        val detailedViewModelFactory = DetailedViewModelFactory(argument!!.id, database)

        binding.lifecycleOwner = this

        viewModel = ViewModelProviders.of(this, detailedViewModelFactory).get(DetailedViewModel::class.java)

        return binding.root
    }
}