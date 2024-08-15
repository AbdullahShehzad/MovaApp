package com.example.myapplication.data.repository

import MoviesService
import com.example.myapplication.data.model.ModelMovie
import com.example.myapplication.data.source.local.MovieDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(private val localData: MovieDao, private val remoteData: MoviesService) {

    suspend fun fetchTop10Movies(num: Int): List<ModelMovie> {
        val response = withContext(Dispatchers.IO) {
            remoteData.getMovies("top_rated", num)
        }

        return response.body()?.getAsJsonArray("results")?.map {
            ModelMovie(
                id = it.asJsonObject.getAsJsonPrimitive("id").asInt,
                url = it.asJsonObject.getAsJsonPrimitive("poster_path").asString,
                rating = it.asJsonObject.getAsJsonPrimitive("vote_average").asDouble,
                name = it.asJsonObject.getAsJsonPrimitive("title").asString
            )
        } ?: emptyList()
    }

    suspend fun fetchNewReleases(num: Int): List<ModelMovie> {
        val response = withContext(Dispatchers.IO) {
            remoteData.getMovies("now_playing", num)
        }
        return response.body()?.getAsJsonArray("results")?.map {
            ModelMovie(
                id = it.asJsonObject.getAsJsonPrimitive("id").asInt,
                url = it.asJsonObject.getAsJsonPrimitive("poster_path").asString,
                rating = it.asJsonObject.getAsJsonPrimitive("vote_average").asDouble,
                name = it.asJsonObject.getAsJsonPrimitive("title").asString
            )
        } ?: emptyList()
    }

    suspend fun filteredMovies(title: String, pageNumFilter: Int): List<ModelMovie> {
        val response = withContext(Dispatchers.IO) {
            remoteData.filterMovies(title, pageNumFilter)
        }
        var images: List<ModelMovie> = emptyList()

        response.body()?.getAsJsonArray("results")?.let { results ->
            images = results.map { result ->
                val jsonObject = result.asJsonObject
                val id = jsonObject.getAsJsonPrimitive("id").asInt
                val posterPath =
                    if (jsonObject.get("poster_path") != null && !jsonObject.get("poster_path").isJsonNull) {
                        jsonObject.getAsJsonPrimitive("poster_path").asString
                    } else if (jsonObject.get("backdrop_path") != null && !jsonObject.get("backdrop_path").isJsonNull) {
                        jsonObject.getAsJsonPrimitive("backdrop_path").asString
                    } else {
                        null  // Image Place Holder.
                    }
                val rating =
                    if (jsonObject.get("vote_average") != null && !jsonObject.get("vote_average").isJsonNull) {
                        jsonObject.getAsJsonPrimitive("vote_average").asDouble
                    } else {
                        0.00
                    }
                val movieName = jsonObject.getAsJsonPrimitive("title").asString
                ModelMovie(
                    id = id, url = posterPath, rating = rating, name = movieName
                )
            }
        }
        return images
    }

    suspend fun advancedFilteredMovies(
        region: String, genre: String, year: Int, sort: String, advPageFilter: Int
    ): List<ModelMovie> {
        val response = withContext(Dispatchers.IO) {
            remoteData.advancedMovieFilter(
                region, genre, year, sort, advPageFilter
            )
        }
        var images: List<ModelMovie> = emptyList()
        response.body()?.getAsJsonArray("results")?.let { results ->
            images = results.map { result ->
                val jsonObject = result.asJsonObject
                val id = jsonObject.getAsJsonPrimitive("id").asInt
                val posterPath =
                    if (jsonObject.get("poster_path") != null && !jsonObject.get("poster_path").isJsonNull) {
                        jsonObject.getAsJsonPrimitive("poster_path").asString
                    } else if (jsonObject.get("backdrop_path") != null && !jsonObject.get("backdrop_path").isJsonNull) {
                        jsonObject.getAsJsonPrimitive("backdrop_path").asString
                    } else {
                        null  // Image Place Holder.
                    }
                val rating =
                    if (jsonObject.get("vote_average") != null && !jsonObject.get("vote_average").isJsonNull) {
                        jsonObject.getAsJsonPrimitive("vote_average").asDouble
                    } else {
                        0.00
                    }
                val movieName = jsonObject.getAsJsonPrimitive("title").asString
                ModelMovie(
                    id = id, url = posterPath, rating = rating, name = movieName
                )
            }
        }
        return images
    }

    suspend fun addMovieToDatabase(
        imageId: Int, imageURL: String?, imageRating: Double, movieName: String
    ) {
        withContext(Dispatchers.IO) {
            localData.insertAll(ModelMovie(imageId, imageURL, imageRating, movieName))
        }
    }

    suspend fun removeMovieFromDB(
        imageId: Int, imageURL: String?, imageRating: Double, movieName: String
    ) {
        withContext(Dispatchers.IO) {
            localData.delete(ModelMovie(imageId, imageURL, imageRating, movieName))
        }
    }
}