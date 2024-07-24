package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainScreen : Fragment(R.layout.fragment_screen_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.navbar)

        //THE CORRECT LOGIC (FINALLY)
        with(bottomNavigationView) {
            setOnItemSelectedListener { true }

            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.action_explore -> {
                        val f = childFragmentManager.findFragmentByTag(Explore.TAG)
                        val h = childFragmentManager.findFragmentByTag(HomeScreen.TAG)
                        if (f == null) {
                            childFragmentManager.commit {
                                add <Explore>(R.id.mainScreenFrag, Explore.TAG)
                            }
                        } else {
                            childFragmentManager.commit {
                                show(f)
                                if (h != null) {
                                    hide(h)
                                }
                            }
                        }
                        true
                    }

                    R.id.action_home -> {
                        val f = childFragmentManager.findFragmentByTag(HomeScreen.TAG)
                        val h = childFragmentManager.findFragmentByTag(Explore.TAG)
                        if (f == null) {
                            childFragmentManager.commit {
                                add<HomeScreen>(R.id.mainScreenFrag, HomeScreen.TAG)
                            }
                        } else {
                            childFragmentManager.commit {
                                show(f)
                                if (h != null) {
                                    hide(h)
                                }
                            }
                        }
                        true
                    }

                    else -> false
                }
            }
        }
    }

    companion object{
        const val TAG = "MainScreen"
    }
}
