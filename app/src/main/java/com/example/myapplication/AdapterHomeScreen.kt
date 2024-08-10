package com.example.myapplication

class AdapterHomeScreen(private val listener: RecyclerViewEvent) : AdapterMovies(R.layout.rv_image, listener) {
    override fun getItemCount(): Int {
        return minOf(20, imageArray.size)
    }
}