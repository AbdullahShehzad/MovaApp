package com.example.myapplication.util

import MoviesService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//This class is made so that we can make api calls to the network.

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        val newRequest =
            originalRequest.newBuilder().addHeader("accept", "application/json").addHeader(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzMTYzNzMwODEyMjRjZDY1NGU5NzExNThkYzQxZGM1MSIsIm5iZiI6MTcyMDQzOTMxNC4xNzQ1MSwic3ViIjoiNjY4YmFhMTA1NmUwY2VmZjY4YWU2YWRlIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.lZTtGh28vnrgP15ol2V8P0uM_CN-A9krrzu-UHVpXQw"
                ).build()
        return chain.proceed(newRequest)
    }
}

val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()

object Network {
    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

    val movieService: MoviesService =
        retrofit.create(MoviesService::class.java) //Implements the MoviesServices class using the retrofit instance
}