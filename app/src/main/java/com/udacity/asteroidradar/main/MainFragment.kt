package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.FilterAsteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)
    }


    private val asteroidAdapter = AsteroidAdapter(AsteroidAdapter.OnClickListener { asteroid ->
        mainViewModel.displayAsteroidDetails(asteroid)
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = mainViewModel
        binding.asteroidRecycler.adapter = asteroidAdapter

        mainViewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                mainViewModel.displayAsteroidDetailsComplete()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       binding.statusLoadingWheel.visibility = View.GONE

        mainViewModel.asteroidList.observe(viewLifecycleOwner, Observer<List<Asteroid>> { asteroid ->
            asteroid.apply {
                asteroidAdapter.submitList(this)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mainViewModel.onChangeFilter(
            when (item.itemId) {
                R.id.show_rent_menu -> {
                    FilterAsteroid.TODAY
                }
                R.id.show_all_menu -> {
                    FilterAsteroid.WEEK
                }
                else -> {
                    FilterAsteroid.ALL
                }
            }
        )
        return true
    }
}
