package com.myapp.myapplicationmuvie.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.myapp.myapplicationmuvie.modelViews.NavigateViewModel
import com.myapp.myapplicationmuvie.R
import com.myapp.myapplicationmuvie.databinding.NavigateFragmentBinding

class NavigateFragment : Fragment() {

    companion object {
        fun newInstance() = NavigateFragment()
    }

    private lateinit var viewModel: NavigateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding:NavigateFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.navigate_fragment,
            container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProviders.of(this).get(NavigateViewModel::class.java)

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.home_item -> {
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragmentContainer, HomeFragment())
                    transaction?.commit()
                }
                R.id.sessions_item -> {
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragmentContainer, SessionsFragment())
                    transaction?.commit()
                }
                R.id.favorite_item -> {
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragmentContainer, FavoritesFragment())
                    transaction?.commit()
                }
                R.id.settings_item -> {
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragmentContainer, SettingsFragment())
                    transaction?.commit()
                }
                else -> {
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragmentContainer, HomeFragment())
                    transaction?.commit()
                }
            }
            binding.myDrawer.isDrawerVisible(GravityCompat.START)
        }

        return binding.root
    }

}