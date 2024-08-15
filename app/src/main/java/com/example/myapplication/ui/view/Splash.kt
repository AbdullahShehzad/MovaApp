package com.example.myapplication.ui.view

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
import com.example.myapplication.R
import com.example.myapplication.ui.viewmodel.ViewModelMovies
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class Splash : Fragment(R.layout.fragment_screen_splash) {
    private val viewModel by activityViewModels<ViewModelMovies>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        rotateImage(view)
    }

    private fun rotateImage(view: View) {
        val loadingIcon = view.findViewById<ImageView>(R.id.loadingIcon)
        loadingIcon.animate().rotation(720f).setDuration(2000).start()
    }

    private fun addObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    combine(viewModel.topMovies, viewModel.newReleases) { topMovies, newReleases ->
                        return@combine Pair(topMovies, newReleases)
                    }.collectLatest {
                        if (it.first.isEmpty()) {
                            return@collectLatest
                        }
                        parentFragmentManager.commit {
                            setCustomAnimations(
                                R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out
                            )
                            replace<MainScreen>(R.id.parentFragment, MainScreen.TAG)
                        }
                    }
                }
            }
        }
    }
}