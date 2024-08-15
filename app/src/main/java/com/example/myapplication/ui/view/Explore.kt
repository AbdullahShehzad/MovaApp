package com.example.myapplication.ui.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.view.adapter.AdapterMovies
import com.example.myapplication.ui.viewmodel.ViewModelExplore
import kotlinx.coroutines.launch

class Explore : Fragment(R.layout.fragment_screen_explore), AdapterMovies.RecyclerViewEvent {

    private val adapterTopMovies: AdapterMovies = AdapterMovies(R.layout.rv_expanded_image, this)
    private val viewModel by activityViewModels<ViewModelExplore>()
    private lateinit var bottomSheet: BottomSheet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheet = BottomSheet()
        addObservers()
        val editText = view.findViewById<EditText>(R.id.searchField)

        editText.doAfterTextChanged {
            it ?: return@doAfterTextChanged
            viewModel.changeFilterNum()
            if (it.isEmpty()) {
                viewModel.clearData()
                viewModel.fetchTop10Movies(1)
            } else {
                viewModel.filteredMovies(it.toString())
            }
        }

        view.findViewById<RecyclerView>(R.id.rv_explore).apply {
            adapter = adapterTopMovies
            //PAGINATION LOGIC FOR TOP RATE MOVIES
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        if (editText.text.isNotEmpty()) {
                            viewModel.filteredMovies(editText.text.toString())
                            return
                        }
                        if (viewModel.topMovies.value.isNotEmpty()) viewModel.fetchTop10Movies()
                    }
                }

            })
        }

        view.findViewById<View>(R.id.filter).setOnClickListener {
            editText.text.clear()
            if (!bottomSheet.isAdded) {
                bottomSheet.show(parentFragmentManager, BottomSheet::class.java.simpleName)
            }
        }

    }

    private fun addObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
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
        const val TAG = "Explore"
    }

    override fun onItemClick(
        position: Int, imageId: Int, imageURL: String?, imageRatings: Double, movieName: String
    ) {
        for (movie in viewModel.myList.value) {
            if (movie.id == imageId) {
                Toast.makeText(
                    this.context, "This movie is already in your list.", Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        viewModel.addMovieToDB(imageId, imageURL, imageRatings, movieName)
        Toast.makeText(this.context, "$movieName added to your list.", Toast.LENGTH_LONG).show()
    }
}
