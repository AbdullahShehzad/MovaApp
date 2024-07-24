package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HomeScreen : Fragment(R.layout.fragment_screen_home) {
    private val adapterTopMovies = AdapterHomeScreen()
    private val adapterNewRelease = AdapterHomeScreen()

    private val viewModel by activityViewModels<ViewModelMovies>()
    private lateinit var rvTopMovies: RecyclerView
    private lateinit var rvNewReleases: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addObservers()
        rvNewReleases = view.findViewById<RecyclerView?>(R.id.recyclerView2)
        rvTopMovies = view.findViewById<RecyclerView?>(R.id.recyclerView1)

        rvTopMovies.apply { adapter = adapterTopMovies }
        rvNewReleases.apply { adapter = adapterNewRelease }

        view.findViewById<TextView>(R.id.see_all1)?.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                addToBackStack(TopMovies.TAG)
                add<TopMovies>(R.id.parentFragment, TopMovies.TAG)
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

                launch {
                        viewModel.newReleases.collect { // does the same thing as the above function but for newReleases array.
                            adapterNewRelease.populateArray(it)
                        }
                    }
            }
        }
    }

    companion object {
        const val TAG = "Home"
    }
}