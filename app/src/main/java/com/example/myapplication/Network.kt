package com.example.myapplication

import com.google.gson.JsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("accept", "application/json")
            .addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzMTYzNzMwODEyMjRjZDY1NGU5NzExNThkYzQxZGM1MSIsIm5iZiI6MTcyMDQzOTMxNC4xNzQ1MSwic3ViIjoiNjY4YmFhMTA1NmUwY2VmZjY4YWU2YWRlIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.lZTtGh28vnrgP15ol2V8P0uM_CN-A9krrzu-UHVpXQw"
            )
            .build()
        return chain.proceed(newRequest)
    }
}

val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()

object Network {
    private val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val movieService: MoviesService =
        retrofit.create(MoviesService::class.java) //Implements the MoviesServices class using the retrofit instance
}

interface MoviesService {
    @GET("movie/top_rated")
    suspend fun getTop10Movies(@Query("page") page: Int): Response<JsonObject>

    @GET("movie/now_playing")
    suspend fun getNewReleases(@Query("page") page: Int): Response<JsonObject>

    @GET("search/movie")
    suspend fun FilterMovies(
        @Query("query") title: String,
        @Query("page") page: Int
    ): Response<JsonObject>

    @GET("discover/movie")
    suspend fun advancedMovieFilter(
        @Query("region") region: String,
        @Query("with_genres") genres: String,
        @Query("primary_release_year") year: Int,
        @Query("sort_by") sort: String,
        @Query("page") page: Int
    ): Response<JsonObject>

}