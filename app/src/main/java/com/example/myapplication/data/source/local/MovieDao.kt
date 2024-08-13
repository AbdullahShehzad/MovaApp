package com.example.myapplication.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.model.ModelMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    fun insertAll(movie: ModelMovie)

    @Delete
    fun delete(movie: ModelMovie)

    @Query("SELECT * FROM Movies")
    fun getAll(): Flow<List<ModelMovie>>

    @Query("DELETE FROM Movies")
    fun deleteAll()
}