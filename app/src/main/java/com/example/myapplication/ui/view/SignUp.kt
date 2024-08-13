package com.example.myapplication.ui.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.myapplication.R

class SignUp : Fragment(R.layout.fragment_screen_signup) {
    private lateinit var signInButton: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInButton = view.findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace<LogIn>(R.id.main)
            }
        }
    }
}