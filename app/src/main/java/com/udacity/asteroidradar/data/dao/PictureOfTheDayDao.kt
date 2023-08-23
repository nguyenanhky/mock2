package com.udacity.asteroidradar.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.entities.PictureOfTheDayEntity
import com.udacity.asteroidradar.data.model.PictureOfTheDay


@Dao
interface PictureOfTheDayDao {
    @Query("select*from picture_of_the_day_table")
    fun getPictureOfTheDay():LiveData<PictureOfTheDayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertAll(vararg pictureOfTheDayEntity: PictureOfTheDayEntity)

}