package com.udacity.asteroidradar.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "picture_of_the_day_table")
@Parcelize
data class PictureOfTheDayEntity(
    @PrimaryKey
    val id: Long = 0L,
    val url: String,
    val mediaType: String,
    val title: String,
) : Parcelable