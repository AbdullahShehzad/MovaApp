import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {
    @GET("movie/top_rated")
    suspend fun getTop10Movies(@Query("page") page: Int): Response<JsonObject>

    @GET("movie/now_playing")
    suspend fun getNewReleases(@Query("page") page: Int): Response<JsonObject>

    @GET("search/movie")
    suspend fun filterMovies(
        @Query("query") title: String, @Query("page") page: Int
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