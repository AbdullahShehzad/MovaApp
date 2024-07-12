import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ModelImage
import com.example.myapplication.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {

    private val _topMovies = MutableStateFlow<List<ModelImage>>(emptyList())
    val topMovies: StateFlow<List<ModelImage>> = _topMovies.asStateFlow()

    private val _newReleases = MutableStateFlow<List<ModelImage>>(emptyList())
    val newReleases: StateFlow<List<ModelImage>> = _newReleases.asStateFlow()

    private var pageNumTopRated = 1
    private var pageNumNewRelease = 1

    fun dataInit(string: String = "") {
        if (string == "topRated") {
            pageNumTopRated += 1
            fetchTop10Movies(pageNumTopRated)
        } else if (string == "newRelease") {
            pageNumNewRelease += 1
            fetchNewReleases(pageNumNewRelease)
        } else {
            fetchTop10Movies(1)
            fetchNewReleases(1)
        }

    }

    private fun fetchTop10Movies(pageNum: Int) {
        viewModelScope.launch {
            val response = Network.movieService.getTop10Movies(pageNum)
            if (response.isSuccessful) {
                val body = response.body()
                body?.getAsJsonArray("results")?.let { results ->
                    val images =
                        results.map { ModelImage(it.asJsonObject.getAsJsonPrimitive("poster_path").asString) }
                    _topMovies.value = images
                }
            }
        }
    }

    private fun fetchNewReleases(pageNum: Int) {
        viewModelScope.launch {
            val response = Network.movieService.getNewReleases(pageNum)
            if (response.isSuccessful) {
                val body = response.body()
                body?.getAsJsonArray("results")?.let { results ->
                    val images =
                        results.map { ModelImage(it.asJsonObject.getAsJsonPrimitive("poster_path").asString) }
                    _newReleases.value = images
                }
            }
        }
    }
}
