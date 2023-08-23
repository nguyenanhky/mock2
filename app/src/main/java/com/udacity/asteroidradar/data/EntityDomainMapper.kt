package com.udacity.asteroidradar.data

import com.udacity.asteroidradar.data.entities.AsteroidEntity
import com.udacity.asteroidradar.data.entities.PictureOfTheDayEntity
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.data.model.PictureOfTheDay


/**
 * Asteroid
 */
fun List<Asteroid>.toDatabaseModel(): Array<AsteroidEntity>{

    return map{
        AsteroidEntity(
            id = it.id,
            codeName = it.codename,
            closeApproachDate = it.closeApproachDate,
            absMagnitude = it.absoluteMagnitude,
            estDiaMax = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()

}

fun List<AsteroidEntity>.toDomainModel(): List<Asteroid>{
    return map{
        Asteroid(
            id = it.id,
            codename = it.codeName,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absMagnitude,
            estimatedDiameter = it.estDiaMax,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isHazardous
        )
    }
}

/**
 * PictureOfTheDay
 */
fun PictureOfTheDay.toDatabaseModel():PictureOfTheDayEntity{
    return PictureOfTheDayEntity(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}

fun PictureOfTheDayEntity.toDomainModel(): PictureOfTheDay {
    return PictureOfTheDay(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}