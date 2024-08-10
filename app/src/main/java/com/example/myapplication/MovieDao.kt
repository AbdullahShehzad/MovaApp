package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    fun insertAll(movie: ModelImage)

    @Delete
    fun delete(movie: ModelImage)

    @Query("SELECT * FROM Movies")
    fun getAll(): Flow<List<ModelImage>>

    @Query("DELETE FROM Movies")
    fun deleteAll()
}