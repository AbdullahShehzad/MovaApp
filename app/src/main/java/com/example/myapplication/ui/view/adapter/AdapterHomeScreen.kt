package com.example.myapplication.ui.view.adapter

import com.example.myapplication.R

class AdapterHomeScreen(listener: RecyclerViewEvent) : AdapterMovies(R.layout.rv_image, listener) {
    override fun getItemCount(): Int {
        return minOf(20, imageArray.size)
    }
}