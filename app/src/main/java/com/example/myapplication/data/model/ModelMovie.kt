package com.example.myapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class ModelMovie(
    @PrimaryKey @ColumnInfo(name = "movie_Cover_URL") val url: String = "",
    @ColumnInfo(name = "movie_Rating") val rating: Double = 0.00,
    @ColumnInfo(name = "movie_Name") val name: String = ""
)