package com.example.myapplication.ui.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.model.ModelMovie
import com.example.myapplication.R

open class AdapterMyList(private val listener: RecyclerViewEvent) :
    RecyclerView.Adapter<AdapterMyList.ViewHolder>() {

    private val BASE_URL_IMG: String = "https://image.tmdb.org/t/p/w500"
    val imageArray: MutableList<ModelMovie> = mutableListOf()

    interface RecyclerViewEvent {
        fun onItemClick(
            position: Int,
            imageId: Int,
            imageURL: String?,
            imageRatings: Double,
            movieName: String
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_expanded_image_my_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return imageArray.size
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentImage = imageArray[position]
        Glide.with(holder.itemView.context)
            .load(BASE_URL_IMG + currentImage.url).error(R.drawable.ic_no_image)
            .into(holder.titleImage)


        holder.titleRating.text = String.format("%.1f", currentImage.rating)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val titleImage: ImageView = itemView.findViewById(R.id.cardImageList)
        val titleRating: TextView = itemView.findViewById(R.id.ratingList)
        private val removeFromListButton: ImageView = itemView.findViewById(R.id.removeFromList)

        init {
            removeFromListButton.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(
                    position,
                    imageArray[position].id,
                    imageArray[position].url,
                    imageArray[position].rating,
                    imageArray[position].name
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