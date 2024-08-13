package com.example.myapplication.ui.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.model.ModelMovie
import com.example.myapplication.R

open class AdapterMovies(
    @LayoutRes private val layout: Int, private val listener: RecyclerViewEvent
) : RecyclerView.Adapter<AdapterMovies.ViewHolder>() {

    private val BASE_URL_IMG: String = "https://image.tmdb.org/t/p/w500"
    val imageArray: MutableList<ModelMovie> = mutableListOf()

    interface RecyclerViewEvent {
        fun onItemClick(position: Int, imageURL: String, imageRatings: Double, movieName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return imageArray.size
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentImage = imageArray[position]
        if (currentImage.url != "null") Glide.with(holder.itemView.context)
            .load(BASE_URL_IMG + currentImage.url).into(holder.titleImage)
        else Glide.with(holder.itemView.context).load(R.drawable.ic_no_image)
            .into(holder.titleImage)

        holder.titleRating.text = String.format("%.1f", currentImage.rating)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val titleImage: ImageView = itemView.findViewById(R.id.cardImage)
        val titleRating: TextView = itemView.findViewById(R.id.rating)
        private val addToListButton: ImageView = itemView.findViewById(R.id.addToListButton)

        init {
            addToListButton.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(
                    position, imageArray[position].url, imageArray[position].rating, imageArray[position].name
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun populateArray(data: List<ModelMovie>) {
        imageArray.clear()
        imageArray.addAll(data)
        notifyDataSetChanged()
    }
}