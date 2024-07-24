package com.example.myapplication

import androidx.annotation.LayoutRes
import kotlin.math.min

class AdapterHomeScreen : AdapterMovies(R.layout.rv_image) {
    override fun getItemCount(): Int {
        return minOf(20, imageArray.size)
    }
}