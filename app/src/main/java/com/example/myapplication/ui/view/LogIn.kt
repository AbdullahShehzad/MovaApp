package com.example.myapplication.ui.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.myapplication.R

class LogIn : Fragment(R.layout.fragment_screen_login) {
    private lateinit var signUpButton: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpButton = view.findViewById(R.id.sign_up_button)
        signUpButton.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace<SignUp>(R.id.main)
            }
        }
    }

}