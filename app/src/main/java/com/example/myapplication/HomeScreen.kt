package com.example.myapplication

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreen : Fragment(R.layout.fragment_screen_home) {
    private val adapterTopMovies = AdapterHomeScreen()
    private val adapterNewRelease = AdapterHomeScreen()

    private val viewModel by activityViewModels<ViewModelMovies>()
    private lateinit var rvTopMovies: RecyclerView
    private lateinit var rvNewReleases: RecyclerView

    private val BASE_URL_IMG: String = "https://image.tmdb.org/t/p/w500"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addObservers(view)
        rvNewReleases = view.findViewById(R.id.recyclerView2)
        rvTopMovies = view.findViewById(R.id.recyclerView1)

        rvTopMovies.apply { adapter = adapterTopMovies }
        rvNewReleases.apply { adapter = adapterNewRelease }

        view.findViewById<TextView>(R.id.see_all1)?.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                addToBackStack(TopMovies.TAG)
                add<TopMovies>(R.id.parentFragment, TopMovies.TAG)
            }
        }

        view.findViewById<TextView>(R.id.see_all2)?.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                addToBackStack(NewReleases.TAG)
                add<NewReleases>(R.id.parentFragment, NewReleases.TAG)
            }
        }
    }

    suspend fun mainImage(view: View) {
        val homeImage1 = view.findViewById<ImageView>(R.id.homeImage1)
        val homeImage2 = view.findViewById<ImageView>(R.id.homeImage2)
        val imageViews = listOf(homeImage1, homeImage2)
        var currentImageViewIndex = 0

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true)
                    for (image in viewModel.newReleases.value) {
                        view.findViewById<TextView>(R.id.movieName).text = image.name
                        val currentImageView = imageViews[currentImageViewIndex]
                        val nextImageView = imageViews[1 - currentImageViewIndex]
                        nextImageView.visibility = View.VISIBLE
                        val currentImageURL = image.url

                        Glide.with(nextImageView.context)
                            .load(BASE_URL_IMG + currentImageURL)
                            .into(nextImageView)

                        // Animate the current image out to the left
                        val slideOut = ObjectAnimator.ofFloat(
                            currentImageView,
                            "translationX",
                            0f,
                            -view.width.toFloat()
                        )
                        slideOut.duration = 500

                        // Animate the next image in from the right
                        val slideIn = ObjectAnimator.ofFloat(
                            nextImageView,
                            "translationX",
                            view.width.toFloat(),
                            0f
                        )
                        slideIn.duration = 500

                        // Start animations
                        slideOut.start()
                        slideIn.start()

                        delay(500) // Wait for the animations to finish

                        // Swap the visibility and reset the translation
                        currentImageView.visibility = View.GONE
                        currentImageView.translationX = 0f

                        currentImageViewIndex =
                            1 - currentImageViewIndex  // Swap the current image index

                        delay(7000) // Delay for 7 seconds before loading the next image
                    }
            }
        }
    }


    private fun addObservers(view: View) {
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
                        mainImage(view)
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "Home"
    }
}