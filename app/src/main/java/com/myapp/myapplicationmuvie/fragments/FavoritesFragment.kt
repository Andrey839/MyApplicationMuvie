package com.myapp.myapplicationmuvie.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.myapplicationmuvie.modelViews.FavoritesViewModel
import com.myapp.myapplicationmuvie.R
import com.myapp.myapplicationmuvie.adapter.AdapterFavoriteFilm
import com.myapp.myapplicationmuvie.adapter.ListenerCallback
import com.myapp.myapplicationmuvie.database.DatabaseVideo
import com.myapp.myapplicationmuvie.database.Favorite
import com.myapp.myapplicationmuvie.database.getDatabase
import com.myapp.myapplicationmuvie.database.toModel
import com.myapp.myapplicationmuvie.databinding.FavoritesFragmentBinding
import com.myapp.myapplicationmuvie.modelViews.FavoritesViewModelFactory
import com.myapp.myapplicationmuvie.networkService.Api
import com.myapp.myapplicationmuvie.repository.Repository
import kotlinx.coroutines.*

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private var viewModel: FavoritesViewModel? = null

    private var viewModelAdapter: AdapterFavoriteFilm? = null

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FavoritesFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.favorites_fragment, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModelAdapter = AdapterFavoriteFilm(arrayListOf(), ListenerCallback {
            if (findNavController().currentDestination?.id == R.id.favorite_item) {
                this.findNavController()
                    .navigate(FavoritesFragmentDirections.actionFavoriteItemToDetailedFragment(it.toModel()))
            }
        })

        binding.root.findViewById<RecyclerView>(R.id.recycler_view_favorite).apply {
            adapter = viewModelAdapter
        }

            return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val application = requireNotNull(this.activity).application
        val database = getDatabase(application)
        val repository = Repository(database)

        viewModel = ViewModelProviders.of(this, FavoritesViewModelFactory(repository = repository))
            .get(FavoritesViewModel::class.java)

        viewModel?.listFavorites?.observe(viewLifecycleOwner, Observer {
            val arrayList = arrayListOf<Favorite>()
            arrayList.addAll(it)
            viewModelAdapter?.setData(arrayList)
        })
    }


}