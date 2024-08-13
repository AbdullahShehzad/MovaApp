package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.source.local.MovieDatabase

class MovaApp : Application() {

    companion object {
        lateinit var database: MovieDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = MovieDatabase.getDatabase(applicationContext)
    }
}