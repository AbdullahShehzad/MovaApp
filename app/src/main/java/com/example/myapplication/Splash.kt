package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : Fragment(R.layout.fragment_screen_splash) {
    private var isRotating = true
    private val viewModel by activityViewModels<ViewModelMovies>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rotateImage(view)
        addObservers()

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000) //2 seconds delay at least for smooth User experience.
            if (viewModel.topMovies.value.isNotEmpty() && viewModel.newReleases.value.isNotEmpty()) {
                parentFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out
                    )
                    replace<MainScreen>(R.id.parentFragment, MainScreen.TAG)
                }
                isRotating = false
            }
        }
    }

    private fun rotateImage(view: View) {
        val loadingIcon = view.findViewById<ImageView>(R.id.loadingIcon)
        CoroutineScope(Dispatchers.Main).launch {
            while (isRotating) {
                loadingIcon.rotation += 1f
                delay(3) // Delay in milliseconds between each rotation update
            }
        }
    }

    private fun addObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.topMovies.collect {}
                }

                launch {
                    viewModel.newReleases.collect {}
                }
            }
        }
    }
}