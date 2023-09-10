package com.udacity.asteroidradar.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.entities.AsteroidEntity

@Dao
interface AsteroidDao {
    @Query("select * from asteroid_table ORDER BY closeApproachDate DESC")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate = :startDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsDay(startDate: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsDate(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: AsteroidEntity)
}