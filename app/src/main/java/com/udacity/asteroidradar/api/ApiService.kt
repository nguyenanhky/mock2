package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.data.model.PictureOfTheDay
import retrofit2.http.GET
import retrofit2.http.Query
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    fun getAsteroidsAsync(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key")apiKey:String
    ):Deferred<String>
}
interface PictureOfTheDayApiService{
    // https://api.nasa.gov/planetary/apod?api_key=YOUR_API_KEY
    @GET("planetary/apod")
    fun getPictureOfTheDayAsync(@Query("api_key")apikey:String): Deferred<PictureOfTheDay>


}
// create a moshi object
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build();

// Retrofit obj for the Asteroid data
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()



object AsteroidApi{
    val asteroidApiService:AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
    val pictureOdTheDayService : PictureOfTheDayApiService by lazy {
        retrofit.create(PictureOfTheDayApiService::class.java)
    }
}