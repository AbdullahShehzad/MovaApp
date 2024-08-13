package com.example.myapplication.ui.view

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.ModelMovie
import com.example.myapplication.MovaApp
import com.example.myapplication.R
import com.example.myapplication.ui.view.adapter.AdapterMyList
import com.example.myapplication.ui.viewmodel.ViewModelMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyList : Fragment(R.layout.fragment_screen_mylist), AdapterMyList.RecyclerViewEvent {

    private val adapterMyList = AdapterMyList(this)
    private val viewModel by activityViewModels<ViewModelMovies>()

    private lateinit var rvMyList: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addObservers(view)
        rvMyList = view.findViewById(R.id.rv_myList)

        rvMyList.apply { adapter = adapterMyList }
    }

    private fun addObservers(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.myList.collect { //Listens when the myList state is populated in the view model
                    if(it.isEmpty()){
                        view.findViewById<RecyclerView>(R.id.rv_myList).visibility = GONE
                        view.findViewById<ImageView>(R.id.emptyListBanner).visibility = VISIBLE
                    }
                    else{
                        view.findViewById<RecyclerView>(R.id.rv_myList).visibility = VISIBLE
                        view.findViewById<ImageView>(R.id.emptyListBanner).visibility = GONE
                    }
                    adapterMyList.populateArray(it)
                }
            }
        }
    }

    companion object {
        const val TAG = "MyList"
    }

    override fun onItemClick(
        position: Int,
        imageURL: String,
        imageRatings: Double,
        movieName: String
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            MovaApp.database.movieDao().delete(ModelMovie(imageURL, imageRatings, movieName))
        }

        Toast.makeText(this.context, "$movieName is removed from the list.", Toast.LENGTH_LONG)
            .show()

    }
}