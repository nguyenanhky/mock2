package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.FilterAsteroid
import com.udacity.asteroidradar.data.getDatabase
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.repositories.AsteroidRepository
import com.udacity.asteroidradar.repositories.PictureOfTheDayRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    private val database = getDatabase(application)
    private val pictureOfTheDayRepository = PictureOfTheDayRepository(database = database)
    private val asteroidRepository = AsteroidRepository(database = database)

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()

    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private var _filterAsteroid = MutableLiveData(FilterAsteroid.ALL)


    @RequiresApi(Build.VERSION_CODES.O)
    val asteroidList = Transformations.switchMap(_filterAsteroid) {
        when (it!!) {
            FilterAsteroid.WEEK -> asteroidRepository.weekAsteroids
            FilterAsteroid.TODAY -> asteroidRepository.todayAsteroids
            else -> asteroidRepository.asteroids
        }
    }


    init {
        viewModelScope.launch {
            pictureOfTheDayRepository.refreshPictureOfTheDay()
        }
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }
    val asteroid = asteroidRepository.asteroids
    val pictureOfTheDay = pictureOfTheDayRepository.getPictureOfTheDay()

    fun onChangeFilter(filter: FilterAsteroid) {
        _filterAsteroid.postValue(filter)
    }

        // initiate navigation to the detail screen
    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }


    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    /**
     * factory dependency
     */
    class MainViewModelFactory(val app:Application):ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }


}