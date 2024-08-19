package com.example.myapplication.ui.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.view.adapter.AdapterMovies
import com.example.myapplication.ui.viewmodel.ViewModelMovies
import kotlinx.coroutines.launch

class TopMovies : Fragment(R.layout.fragment_screen_top_movies), AdapterMovies.RecyclerViewEvent {

    private val adapterTopMovies: AdapterMovies = AdapterMovies(R.layout.rv_expanded_image, this)
    private val viewModel by activityViewModels<ViewModelMovies>()
    private lateinit var rvTopMovies: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTopMovies = view.findViewById(R.id.rvTopMovies)
        addObservers()

        rvTopMovies.apply {
            adapter = adapterTopMovies

            //PAGINATION LOGIC FOR TOP RATE MOVIES
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.fetchTop10Movies(0)
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
                    viewModel.topMovies.collect { //Listens when the topMovies state is populated in the view model
                        adapterTopMovies.populateArray(it)
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "TopMovies"
    }

    //IMPLEMENT LOGIC TO CALL THE NEW FRAGMENT IN THIS FUNCTION.
    override fun onItemClick(
        position: Int, imageId: Int, imageURL: String?, imageRatings: Double, movieName: String
    ) {
        viewModel.addMovieToDB(this.requireContext(), imageId, imageURL, imageRatings, movieName)
    }
}