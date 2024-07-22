package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class Explore : Fragment(R.layout.fragment_explore) {

    private val adapterTopMovies: CustomAdapter = CustomAdapter(R.layout.rv_expanded_image)
    private val viewModel by viewModels<HomeScreenViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dataInit()
        addObservers()

        view.findViewById<RecyclerView?>(R.id.rv_explore)?.apply {
            adapter = adapterTopMovies

            //PAGINATION LOGIC FOR TOP RATE MOVIES
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        //Implement the new Api call here.
                        viewModel.dataInit("topRated")
                    }
                }
            })
        }

        view.findViewById <EditText> (R.id.searchField).apply {
           setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
               //This applies the filtering action when enter is pressed on the edit text field.
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val inputText = text.toString()
                    Log.d("EMovie name",inputText)
                    return@OnEditorActionListener true
                }
                false
            })
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
}