package com.myapp.myapplicationmuvie.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.myapp.myapplicationmuvie.R
import com.myapp.myapplicationmuvie.adapter.AdapterFilmsNow
import com.myapp.myapplicationmuvie.database.getDatabase
import com.myapp.myapplicationmuvie.databinding.HomeFragmentBinding
import com.myapp.myapplicationmuvie.modelViews.HomeVideoViewModelFactory
import com.myapp.myapplicationmuvie.modelViews.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ilist_item_home.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val database = getDatabase(application)
        ViewModelProviders.of(
            this, HomeVideoViewModelFactory(
                context = requireNotNull(this.activity),
                dataSource = database
            )
        ).get(HomeViewModel::class.java)
    }

    private var booleanValue = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        booleanValue = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: HomeFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)

        binding.lifecycleOwner = this

        val manager = GridLayoutManager(activity, 2)
        val adapter = AdapterFilmsNow {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailedFragment(it))
        }

        binding.recyclerViewHome.adapter = adapter
        binding.recyclerViewHome.apply {
            layoutManager = manager
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL))
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        if (FirebaseAuth.getInstance().currentUser == null) {
            if (findNavController().currentDestination?.id == R.id.home_item) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToRegistrationFragment())
            }
        }

        viewModel.listModel.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                binding.loadSpinner.visibility = View.GONE
                adapter.submitList(it)
            } else {
                binding.loadSpinner.visibility = View.VISIBLE
            }
        })

        return binding.root
    }
}