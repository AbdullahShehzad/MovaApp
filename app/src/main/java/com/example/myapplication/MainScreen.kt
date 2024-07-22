package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainScreen : Fragment(R.layout.fragment_homescreen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.navbar)

        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace <HomeScreen> (R.id.mainScreenFrag, "homeScreen")
            bottomNavigationView.selectedItemId = R.id.action_home
            addToBackStack("homeFrag")
        }

        view.findViewById<View>(R.id.action_explore)?.setOnClickListener {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                replace <Explore> (R.id.mainScreenFrag, "exploreScreen")
                bottomNavigationView.selectedItemId = R.id.action_explore
                addToBackStack("exploreFrag")
            }
        }

        view.findViewById<View>(R.id.action_home)?.setOnClickListener {
            childFragmentManager.popBackStack()
            bottomNavigationView.selectedItemId = R.id.action_home
        }

    }


}