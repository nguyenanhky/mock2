package com.udacity.asteroidradar.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.getFormattedSeventhDay
import com.udacity.asteroidradar.api.getFormattedToday
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.AsteroidsDatabase
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.data.toDatabaseModel
import com.udacity.asteroidradar.data.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsteroidRepository(private val database: AsteroidsDatabase) {


    @RequiresApi(Build.VERSION_CODES.O)
    private val startDate = LocalDateTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val endDate = LocalDateTime.now().minusDays(7)


    // convert your LiveData list of DatabaseVideo objects to model video objects
    val asteroids: LiveData<List<Asteroid>> = MediatorLiveData<List<Asteroid>>().apply {
        addSource(database.asteroidDao.getAsteroids()) { asteroidEntities ->
            val asteroidList = asteroidEntities.toDomainModel()
            value = asteroidList
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val todayAsteroids: LiveData<List<Asteroid>> = MediatorLiveData<List<Asteroid>>().apply {
        addSource(database.asteroidDao.getAsteroidsDay(startDate.format(DateTimeFormatter.ISO_DATE))) {
            val todayAsteroidList = it.toDomainModel()
            value = todayAsteroidList
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    val weekAsteroids: LiveData<List<Asteroid>> = MediatorLiveData<List<Asteroid>>().apply {
        addSource(
            database.asteroidDao.getAsteroidsDate(
                startDate.format(DateTimeFormatter.ISO_DATE),
                endDate.format(DateTimeFormatter.ISO_DATE)
            )
        ) {
            val weekAsteroidList = it.toDomainModel()
            value = weekAsteroidList
        }

    }

    /**
     * update the offline cache
     */
    suspend fun refreshAsteroids() {
        val startDateFormatted = getFormattedToday()
        val endDateFormatted = getFormattedSeventhDay()

        withContext(Dispatchers.IO) {
            val asteroidList = AsteroidApi.asteroidApiService.getAsteroidsAsync(
                startDateFormatted,
                endDateFormatted,
                Constants.API_KEY
            ).await()

            // String-->List<Asteroid>
            val parsedAsteroidsList = parseAsteroidsJsonResult(JSONObject(asteroidList))
            database.asteroidDao.insertAll(*parsedAsteroidsList.toDatabaseModel())
        }
    }
}