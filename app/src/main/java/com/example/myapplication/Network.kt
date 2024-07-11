package com.example.myapplication

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers


object Network
{
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val movieService = retrofit.create(MoviesService::class.java) //Implements the MoviesServices class using the retrofit instance
}

interface MoviesService
{
    @Headers(
        "accept: application/json",
        "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzMTYzNzMwODEyMjRjZDY1NGU5NzExNThkYzQxZGM1MSIsIm5iZiI6MTcyMDQzOTMxNC4xNzQ1MSwic3ViIjoiNjY4YmFhMTA1NmUwY2VmZjY4YWU2YWRlIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.lZTtGh28vnrgP15ol2V8P0uM_CN-A9krrzu-UHVpXQw"
    )
    @GET("movie/top_rated")
    suspend fun getTop10Movies(): Response<JsonObject>

    @Headers(
        "accept: application/json",
        "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzMTYzNzMwODEyMjRjZDY1NGU5NzExNThkYzQxZGM1MSIsIm5iZiI6MTcyMDQzOTMxNC4xNzQ1MSwic3ViIjoiNjY4YmFhMTA1NmUwY2VmZjY4YWU2YWRlIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.lZTtGh28vnrgP15ol2V8P0uM_CN-A9krrzu-UHVpXQw"
    )
    @GET("movie/now_playing")
    suspend fun getNewReleases(): Response <JsonObject>
}