package com.myapp.myapplicationmuvie.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.myapp.myapplicationmuvie.R
import com.myapp.myapplicationmuvie.database.Favorite
import com.myapp.myapplicationmuvie.database.getDatabase
import com.myapp.myapplicationmuvie.databinding.DetailedFragmentBinding
import com.myapp.myapplicationmuvie.modelViews.DetailedViewModel
import com.myapp.myapplicationmuvie.modelViews.DetailedViewModelFactory
import com.myapp.myapplicationmuvie.networkService.TrailerJson
import com.myapp.myapplicationmuvie.repository.Repository
import kotlinx.android.synthetic.main.detailed_fragment.*
import kotlinx.coroutines.*

class DetailedFragment : Fragment() {

    companion object {
        fun newInstance() = DetailedFragment()
    }

    private lateinit var viewModel: DetailedViewModel
    private var url: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getModelId()

        viewModel.snackBoolean.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.view?.let { it1 ->
                    Snackbar.make(
                        it1,
                        resources.getString(R.string.delete_film_with_favorite),
                        Snackbar.LENGTH_LONG
                    ).setAction(
                        resources.getString(
                            R.string.OK
                        )
                    ) {
                        viewModel.deleteFilmWithFavorite()
                        Toast.makeText(
                            this.context,
                            "Фильм удалён из избранного",
                            Toast.LENGTH_LONG
                        ).show()
                    }.show()
                }
            } else Toast.makeText(this.context, "Фильм добавлен", Toast.LENGTH_LONG).show()
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DetailedFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.detailed_fragment,
            container, false
        )

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment_container
            duration = resources.getInteger(R.integer.config_navAnimTime).toLong()
            scrimColor = Color.TRANSPARENT
//            setAllContainerColors(requireContext().theme.resolveAttribute(R.attr.colorOnSurface, true))
        }

        val application = requireNotNull(this.activity).application
        val database = getDatabase(application)
        val repository = Repository(database)
        val argument = arguments?.let { DetailedFragmentArgs.fromBundle(it) }

        val detailedViewModelFactory = argument?.id?.let { DetailedViewModelFactory(it, repository) }

        binding.lifecycleOwner = this

        viewModel =
            ViewModelProviders.of(this, detailedViewModelFactory).get(DetailedViewModel::class.java)

        viewModel.favoritesValue.observe(viewLifecycleOwner, Observer {
            viewModel.addOrDelete = it != null
        })

        binding.viewModel = viewModel

        viewModel.urlTrailer.observe(viewLifecycleOwner, Observer {
            if (it[0].trailer.isNotEmpty()) {
                binding.watchTrailerFilm.visibility = View.VISIBLE
                val http = "http:/"
                val string = it[0].trailer[0].substringAfter('/')
                val urlString = string.substringBefore('"')
                url = http + urlString
            }
        })

        binding.watchTrailerFilm.setOnClickListener {
            startIntent()
        }

        return binding.root
    }

    private fun startIntent() {
        val packageManager = context?.packageManager
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(url)
        }
        if (packageManager?.let { intent.resolveActivity(it) } != null) {
            startActivity(intent)
        }
    }
}