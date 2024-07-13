package com.example.myapplication

import HomeScreenViewModel
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HomeScreenScroll : Fragment(R.layout.frag_homescreen_scroll) {
    private val adapterTopMovies: CustomAdapter = CustomAdapter(R.layout.rv_image)
    private val adapterNewReleases: CustomAdapter = CustomAdapter(R.layout.rv_image)

    private val viewModel by viewModels<HomeScreenViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dataInit()
        addObservers()

        view.findViewById<RecyclerView?>(R.id.recyclerView1)?.apply { adapter = adapterTopMovies }
        view.findViewById<RecyclerView?>(R.id.recyclerView2)?.apply { adapter = adapterNewReleases }

        view.findViewById<TextView>(R.id.see_all1)?.setOnClickListener {
            parentFragment?.parentFragmentManager?.commit {
                addToBackStack(null)
                replace<TopMovies>(R.id.parentFragment)
            }
        }
    }

    private fun addObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.topMovies.collect { //Listens when the topMovies state is populated in the view model
                        adapterTopMovies.populateArray(it)
                    }
                }

                launch {
                    viewModel.newReleases.collect { // does the same thing as the above function but for newReleases array.
                        adapterNewReleases.populateArray(it)
                    }
                }
            }
        }
    }
}