package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class Explore : Fragment(R.layout.fragment_screen_explore) {

    private val adapterTopMovies: AdapterMovies = AdapterMovies(R.layout.rv_expanded_image)
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
            }
            else {
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
                        //Implement the new Api call here.
                        if (editText.text.isNotEmpty()) {
                            viewModel.filteredMovies(editText.text.toString())
                            return
                        }
                        if(viewModel.topMovies.value.isNotEmpty())
                            viewModel.fetchTop10Movies()
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
}
