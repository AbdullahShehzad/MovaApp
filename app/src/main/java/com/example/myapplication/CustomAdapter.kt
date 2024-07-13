package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CustomAdapter(@LayoutRes private val layout: Int) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private val BASE_URL_IMG: String = "https://image.tmdb.org/t/p/w500"
    private val imageArray: MutableList<ModelImage> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return imageArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentImage = imageArray[position]
        Glide.with(holder.itemView.context)
            .load(BASE_URL_IMG + currentImage.url)
            .into(holder.titleImage)
    }

    fun populateArray(data: List<ModelImage>) {
        imageArray.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleImage: ImageView = itemView.findViewById(R.id.cardImage)
    }

}