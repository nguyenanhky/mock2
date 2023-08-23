package com.udacity.asteroidradar.data.model

import com.squareup.moshi.Json

data class PictureOfTheDay(
    val url:String,
    @Json(name = "media_type")val mediaType:String,
    val title:String
)