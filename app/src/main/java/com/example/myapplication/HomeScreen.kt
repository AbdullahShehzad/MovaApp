package com.example.myapplication

import HomeScreenViewModel
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch


class HomeScreen : Fragment(R.layout.fragment_homescreen) {

    private val adapter1: CustomAdapter = CustomAdapter()
    private val adapter2: CustomAdapter = CustomAdapter()

    val viewModel: HomeScreenViewModel = HomeScreenViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dataInit()

        addObservers()
        view.findViewById<RecyclerView?>(R.id.recyclerView1)?.apply {
            setHasFixedSize(true)
            adapter = adapter1
        }

        view.findViewById<RecyclerView?>(R.id.recyclerView2)?.apply {
            setHasFixedSize(true)
            adapter = adapter2
        }

        view.findViewById<TextView?>(R.id.see_all1)?.setOnClickListener {
            parentFragmentManager.commit {
                replace <SignUp> (R.id.main)
            }
        }
    }

    fun addObservers(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.topMovies.collect { //Listens when the topmovies state is populated in the view model
                        adapter1.populateArray(it)
                    }
                }

                launch {
                    viewModel.newReleases.collect { // does the same thing as the above function but for newReleases array.
                        adapter2.populateArray(it)
                    }
                }
            }
        }
    }
}