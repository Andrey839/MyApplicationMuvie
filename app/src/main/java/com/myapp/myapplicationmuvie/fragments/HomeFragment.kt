package com.myapp.myapplicationmuvie.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.myapp.myapplicationmuvie.adapter.AdapterFilmsNow
import com.myapp.myapplicationmuvie.adapter.FilmClickListener
import com.myapp.myapplicationmuvie.modelViews.HomeViewModel
import com.myapp.myapplicationmuvie.R
import com.myapp.myapplicationmuvie.database.getDatabase
import com.myapp.myapplicationmuvie.databinding.HomeFragmentBinding
import com.myapp.myapplicationmuvie.modelViews.HomeVideoViewModelFactory

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: HomeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)

        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val database = getDatabase(application)

        val homeViewModelFactory = HomeVideoViewModelFactory(requireNotNull(this.activity), database)

        val viewModel = ViewModelProviders.of(this, homeViewModelFactory).get(HomeViewModel::class.java)

        val manager = GridLayoutManager(activity, 2)

        val adapter = AdapterFilmsNow(FilmClickListener{
             viewModel.detailedInfoFilm(it)
        })

        binding.recyclerViewHome.layoutManager = manager

        binding.recyclerViewHome.adapter = adapter

        viewModel.repository.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.toDetailedInfoFilm.observe(viewLifecycleOwner, Observer {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailedFragment(it))
            viewModel.detailedInfoFilmReset()
        })

        return binding.root
    }

}