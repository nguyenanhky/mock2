package com.udacity.asteroidradar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.data.dao.AsteroidDao
import com.udacity.asteroidradar.data.dao.PictureOfTheDayDao
import com.udacity.asteroidradar.data.entities.AsteroidEntity
import com.udacity.asteroidradar.data.entities.PictureOfTheDayEntity

@Database(entities = [PictureOfTheDayEntity::class,AsteroidEntity::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfTheDayDao: PictureOfTheDayDao
}

// var for our singleton
private lateinit var INSTANCE: AsteroidsDatabase
fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE =
                Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidsDatabase::class.java,
                    "asteroids"
                ).build()
        }
    }
    return INSTANCE
}