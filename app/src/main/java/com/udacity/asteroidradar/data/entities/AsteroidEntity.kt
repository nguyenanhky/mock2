package com.udacity.asteroidradar.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.data.model.Asteroid
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "asteroid_table")
@Parcelize
data class AsteroidEntity constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val codeName: String,
    val closeApproachDate: String,
    val absMagnitude: Double,
    val estDiaMax: Double,
    val isHazardous: Boolean,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
): Parcelable
