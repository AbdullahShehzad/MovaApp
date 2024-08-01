package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class BottomSheet : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<ViewModelExplore>()
    private val adapterTopMovies: AdapterMovies = AdapterMovies(R.layout.rv_expanded_image)
    var region = ""
    private var genreChipIds = mutableListOf<Int>()
    private var genre = ""
    private var year: Int = 2024
    private var sort = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true

                view.findViewById<ChipGroup>(R.id.regionChipGroup).apply {//
                    check(viewModel.chipSettings.value.region)//viewModel.regions.value)
                }

                for(i in 1..<(viewModel.chipSettings.value.genre.size - 1))//i in 1..<(viewModel.genre.value.size - 1))
                {
                    view.findViewById<ChipGroup>(R.id.regionChipGroup).apply {//
                        check(viewModel.chipSettings.value.genre[i])//viewModel.genre.value[i])
                    }
                }

                view.findViewById<ChipGroup>(R.id.timeChipGroup).apply {//
                    check(viewModel.chipSettings.value.time)//viewModel.time.value)
                }

                view.findViewById<ChipGroup>(R.id.sortChipGroup).apply {//
                    check(viewModel.chipSettings.value.sort)//viewModel.sort.value)
                }
            }
        }



        applyButtonAction(view)
    }

    private fun applyButtonAction(view: View) {
        //FILTERING LOGIC IS IMPLEMENTED HERE.
        view.findViewById<Button>(R.id.applyButton).setOnClickListener {
            view.findViewById<ChipGroup>(R.id.regionChipGroup).apply {
                viewModel.updateRegion(checkedChipId)
                region = view.findViewById<Chip>(checkedChipId).text.toString()
            }

            view.findViewById<ChipGroup>(R.id.genreChipGroup).apply {
                viewModel.updateGenre(checkedChipIds)
                genreChipIds.clear()
                genreChipIds.addAll(checkedChipIds)
                for (chipId in genreChipIds) {
                    genre += findViewById<Chip>(chipId).contentDescription.toString() + '|'
                }
            }

            view.findViewById<ChipGroup>(R.id.timeChipGroup).apply {
                viewModel.updateTime(checkedChipId)
                val yearString = view.findViewById<Chip>(checkedChipId).text.toString()
                if (yearString != "All Periods") {
                    year = yearString.toInt()
                }
            }

            view.findViewById<ChipGroup>(R.id.sortChipGroup).apply {
                viewModel.updateSort(checkedChipId)
                sort = view.findViewById<Chip>(checkedChipId).text.toString()
            }

            viewModel.advancedMovieFilter(region, genre, year, sort)
        }

        view.findViewById<Button>(R.id.resetButton).setOnClickListener {
            view.findViewById<ChipGroup>(R.id.regionChipGroup).check(R.id.allRegions)
            view.findViewById<ChipGroup>(R.id.genreChipGroup).clearCheck()
            view.findViewById<ChipGroup>(R.id.timeChipGroup).check(R.id.allPeriods)
            view.findViewById<ChipGroup>(R.id.sortChipGroup).check(R.id.popularity)
            viewModel.fetchTop10Movies(1)
        }

    }

    companion object {
        const val TAG = "BottomSheet"
    }
}