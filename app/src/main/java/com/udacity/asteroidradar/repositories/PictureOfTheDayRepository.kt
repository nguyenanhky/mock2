package com.udacity.asteroidradar.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.data.AsteroidsDatabase
import com.udacity.asteroidradar.data.entities.PictureOfTheDayEntity
import com.udacity.asteroidradar.data.model.PictureOfTheDay
import com.udacity.asteroidradar.data.toDatabaseModel
import com.udacity.asteroidradar.data.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.math.log

class PictureOfTheDayRepository(private val database: AsteroidsDatabase) {
    companion object{
        private const val TAG = "PictureOfTheDayRepository"
    }

    // cách sử dụng khác của Transtion.map chuyển đổi kiểu dữ liệu
    fun getPictureOfTheDay(): LiveData<PictureOfTheDay> {
        val pictureOfTheDayDaoLiveData = database.pictureOfTheDayDao.getPictureOfTheDay()

        val pictureOfTheDayLiveData = MutableLiveData<PictureOfTheDay>()

        pictureOfTheDayDaoLiveData.observeForever { pictureOfTheDay ->
           // Log.d(TAG, "$pictureOfTheDay: ")
            pictureOfTheDayLiveData.value = pictureOfTheDay?.toDomainModel()
        }

        return pictureOfTheDayLiveData
    }

    /**
     * update the offline cache
     */
    suspend fun refreshPictureOfTheDay(){
        withContext(Dispatchers.IO){
            try{
                val picture = AsteroidApi.pictureOdTheDayService.getPictureOfTheDayAsync(Constants.API_KEY).await()
                Log.d(TAG, "$picture")
                database.pictureOfTheDayDao.insertAll(picture.toDatabaseModel())
            }catch (e:Exception){
                withContext(Dispatchers.IO){
                    Log.d(TAG, "PictureOfTheDay retrieval unsuccessful: ${e.message}")
                }
            }
        }
    }
}