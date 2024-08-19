package com.example.myapplication.ui.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
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
import com.example.myapplication.R
import com.example.myapplication.ui.view.adapter.AdapterHomeScreen
import com.example.myapplication.ui.view.adapter.AdapterMovies
import com.example.myapplication.ui.viewmodel.ViewModelMovies
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ViewUtils.showKeyboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreen : Fragment(R.layout.fragment_screen_home), AdapterMovies.RecyclerViewEvent {
    private val adapterTopMovies = AdapterHomeScreen(this)
    private val adapterNewRelease = AdapterHomeScreen(this)

    private val viewModel by activityViewModels<ViewModelMovies>()
    private lateinit var rvTopMovies: RecyclerView
    private lateinit var rvNewReleases: RecyclerView

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addObservers(view)
        rvNewReleases = view.findViewById(R.id.recyclerView2)
        rvTopMovies = view.findViewById(R.id.recyclerView1)

        rvTopMovies.apply { adapter = adapterTopMovies }
        rvNewReleases.apply { adapter = adapterNewRelease }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainImage(view)
            }
        }

        view.findViewById<ImageView>(R.id.search).setOnClickListener {
            parentFragmentManager.apply {
                commit {
                    show(findFragmentByTag(Explore.TAG)!!)
                    hide(this@HomeScreen)
                }
            }

            parentFragment?.view?.apply {
                findViewById<BottomNavigationView>(R.id.navbar)?.selectedItemId =
                    R.id.action_explore

                findViewById<EditText>(R.id.searchField)?.apply {
                    requestFocus()
                    selectAll()
                    showKeyboard(view)
                }
            }

        }

        view.findViewById<TextView>(R.id.see_all1)?.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                setCustomAnimations(
                    R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out
                )
                addToBackStack(TopMovies.TAG)
                add<TopMovies>(R.id.parentFragment, TopMovies.TAG)
            }
        }

        view.findViewById<TextView>(R.id.see_all2)?.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                setCustomAnimations(
                    R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out
                )
                addToBackStack(NewReleases.TAG)
                add<NewReleases>(R.id.parentFragment, NewReleases.TAG)
            }
        }
    }

    private suspend fun mainImage(view: View) {
        val homeImage1 = view.findViewById<ImageView>(R.id.homeImage1)
        val homeImage2 = view.findViewById<ImageView>(R.id.homeImage2)
        val imageViews = listOf(homeImage1, homeImage2)
        var currentImageViewIndex = 0

        for (image in viewModel.newReleases.value) {
            view.findViewById<TextView>(R.id.myList).setOnClickListener {
                viewModel.addMovieToDB(
                    this.requireContext(),
                    image.id,
                    image.url,
                    image.rating,
                    image.name
                )
            }

            view.findViewById<TextView>(R.id.movieName).text = image.name
            val currentImageView = imageViews[currentImageViewIndex]
            val nextImageView = imageViews[1 - currentImageViewIndex]
            nextImageView.visibility = View.VISIBLE
            val currentImageURL = image.url

            Glide.with(nextImageView.context).load(BASE_URL_IMG + currentImageURL)
                .into(nextImageView)

            // Animate the current image out to the left
            val slideOut = ObjectAnimator.ofFloat(
                currentImageView, "translationX", 0f, -view.width.toFloat()
            )
            slideOut.duration = 500

            // Animate the next image in from the right
            val slideIn = ObjectAnimator.ofFloat(
                nextImageView, "translationX", view.width.toFloat(), 0f
            )
            slideIn.duration = 500

            // Start animations
            slideOut.start()
            slideIn.start()

            delay(500) // Wait for the animations to finish

            // Swap the visibility and reset the translation
            currentImageView.visibility = View.GONE
            currentImageView.translationX = 0f

            currentImageViewIndex = 1 - currentImageViewIndex  // Swap the current image index

            delay(7000) // Delay for 7 seconds before loading the next image
        }
        mainImage(view)
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
                    }
                }
            }
        }
    }

    override fun onItemClick(
        position: Int, imageId: Int, imageURL: String?, imageRatings: Double, movieName: String
    ) {
        viewModel.addMovieToDB(this.requireContext(), imageId, imageURL, imageRatings, movieName)
    }

    companion object {
        const val TAG = "Home"
        private const val BASE_URL_IMG: String = "https://image.tmdb.org/t/p/w500"
    }
}