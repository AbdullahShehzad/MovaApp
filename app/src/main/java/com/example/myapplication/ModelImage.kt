package com.example.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class ModelImage(
    @PrimaryKey @ColumnInfo(name = "Movie_Cover_URL") val url: String = "",
    @ColumnInfo(name = "Movie_Rating") val rating: Double = 0.00,
    @ColumnInfo(name = "Movie_Name") val name: String = ""
)