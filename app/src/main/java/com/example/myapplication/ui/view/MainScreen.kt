package com.example.myapplication.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Selection.selectAll
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ViewUtils.showKeyboard

class MainScreen : Fragment(R.layout.fragment_screen_main) {
    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.navbar)

        childFragmentManager.apply {
            commit {
                add<MyList>(R.id.mainScreenFrag, MyList.TAG)
                add<Explore>(R.id.mainScreenFrag, Explore.TAG)
                add<HomeScreen>(R.id.mainScreenFrag, HomeScreen.TAG)
            }
        }

        with(bottomNavigationView) {
            selectedItemId = R.id.action_home
            setOnItemSelectedListener { item ->

                when (item.itemId) {
                    R.id.action_explore -> {
                        val exploreFragment = childFragmentManager.findFragmentByTag(Explore.TAG)
                        val homeFragment = childFragmentManager.findFragmentByTag(HomeScreen.TAG)
                        val myListFragment = childFragmentManager.findFragmentByTag(MyList.TAG)

                        if (exploreFragment == null) {
                            childFragmentManager.commit {
                                add<Explore>(R.id.mainScreenFrag, Explore.TAG)
                            }
                        } else {
                            childFragmentManager.commit {
                                show(exploreFragment)
                                if (homeFragment != null) {
                                    hide(homeFragment)
                                }
                                if (myListFragment != null) {
                                    hide(myListFragment)
                                }
                            }
                        }
                        true
                    }

                    R.id.action_home -> {
                        val homeFragment = childFragmentManager.findFragmentByTag(HomeScreen.TAG)
                        val exploreFragment = childFragmentManager.findFragmentByTag(Explore.TAG)
                        val myListFragment = childFragmentManager.findFragmentByTag(MyList.TAG)
                        if (homeFragment == null) {
                            childFragmentManager.commit {
                                add<MyList>(R.id.mainScreenFrag, MyList.TAG)
                                add<Explore>(R.id.mainScreenFrag, Explore.TAG)
                                add<HomeScreen>(R.id.mainScreenFrag, HomeScreen.TAG)
                            }
                        } else {
                            childFragmentManager.commit {
                                show(homeFragment)
                                if (exploreFragment != null) {
                                    hide(exploreFragment)
                                }
                                if (myListFragment != null) {
                                    hide(myListFragment)
                                }
                            }
                        }
                        true
                    }

                    R.id.action_myList -> {
                        val myListFragment = childFragmentManager.findFragmentByTag(MyList.TAG)
                        val exploreFragment = childFragmentManager.findFragmentByTag(Explore.TAG)
                        val homeFragment = childFragmentManager.findFragmentByTag(HomeScreen.TAG)
                        if (myListFragment == null) {
                            childFragmentManager.commit {
                                add<MyList>(R.id.mainScreenFrag, MyList.TAG)
                            }
                        } else {
                            childFragmentManager.commit {
                                show(myListFragment)
                                if (exploreFragment != null) {
                                    hide(exploreFragment)
                                }
                                if (homeFragment != null) {
                                    hide(homeFragment)
                                }
                            }
                        }
                        true
                    }

                    else -> false
                }
            }
        }

//        view.findViewById<ImageView>(R.id.search).setOnClickListener {
//            childFragmentManager.apply {
//                commit {
//                    show(findFragmentByTag(Explore.TAG)!!)
//                    hide(findFragmentByTag(HomeScreen.TAG)!!)
//                }
//            }
//            view.findViewById<EditText>(R.id.searchField).apply {
//                requestFocus()
//                selectAll()
//                showKeyboard(view)
//            }
//        }
    }

    companion object {
        const val TAG = "MainScreen"
    }
}

//                    childFragmentManager.apply {
//                        commit {
//                            val exploreFragment = findFragmentByTag(Explore.TAG)
//                            if (exploreFragment == null) {
//                                add<Explore>(R.id.mainScreenFrag, Explore.TAG)
//                            }
//                            show(findFragmentByTag(Explore.TAG)!!)
//                            hide(findFragmentByTag(HomeScreen.TAG)!!)
//                        }
//                    }
//                    selectedItemId = R.id.action_explore
//                    view.findViewById<EditText>(R.id.searchField).apply {
//                        requestFocus()
//                        selectAll()
//                        showKeyboard(view)
//                    }
//                }
