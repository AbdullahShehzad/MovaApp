package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewReleases : Fragment(R.layout.fragment_screen_top_movies), AdapterMovies.RecyclerViewEvent {

    private val adapterNewReleases: AdapterMovies = AdapterMovies(R.layout.rv_expanded_image, this)
    private val viewModel by activityViewModels<ViewModelMovies>()
    private lateinit var rvNewReleases: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvNewReleases = view.findViewById(R.id.rvTopMovies)
        view.findViewById<TextView>(R.id.topMovies).text = "New Releases"
        addObservers()

        rvNewReleases.apply {
            adapter = adapterNewReleases

            //PAGINATION LOGIC FOR TOP RATE MOVIES
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.fetchNewReleases()
                    }
                }
            })
        }

        val backButton = view.findViewById<ImageView>(R.id.back)
        backButton.setOnClickListener {
            parentFragmentManager.apply {
                popBackStack()
            }
        }
    }


    private fun addObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.newReleases.collect { //Listens when the topMovies state is populated in the view model
                        adapterNewReleases.populateArray(it)
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "NewReleases"
    }

    override fun onItemClick(
        position: Int, imageURL: String, imageRatings: Double, movieName: String
    ) {
        for (movie in viewModel.myList.value) {
            if (movie.url == imageURL) {
                Toast.makeText(
                    this.context, "This movie is already in your list.", Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            MovaApp.database.movieDao().insertAll(ModelImage(imageURL, imageRatings, movieName))
        }
        Toast.makeText(this.context, "$movieName added to your list.", Toast.LENGTH_LONG)
            .show()
    }
}