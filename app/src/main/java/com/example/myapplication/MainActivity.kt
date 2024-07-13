package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the content go behind the status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars =
            false // Adjust if you want light or dark status bar icons
        window.statusBarColor = getColor(android.R.color.transparent)

        //Fragment population.
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add<HomeScreen>(R.id.parentFragment)
            }
        }

    }
}