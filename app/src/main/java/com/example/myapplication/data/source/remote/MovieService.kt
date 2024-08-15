import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {
    @GET("movie/{path}")
    suspend fun getMovies(
        @Path("path") path: String,
        @Query("page") page: Int
    ): Response<JsonObject>

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