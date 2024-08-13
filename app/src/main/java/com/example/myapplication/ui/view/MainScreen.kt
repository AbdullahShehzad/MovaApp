package com.example.myapplication.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
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

        with(bottomNavigationView) {
            setOnItemSelectedListener { true }

            setOnItemSelectedListener { item ->
                view.findViewById<ImageView>(R.id.search).setOnClickListener {
                    selectedItemId = R.id.action_explore
                    view.findViewById<EditText>(R.id.searchField).apply {
                        requestFocus()
                        selectAll()
                        showKeyboard(view)
                    }

                }

                when (item.itemId) {
                    R.id.action_explore -> {
                        val f = childFragmentManager.findFragmentByTag(Explore.TAG)
                        val h = childFragmentManager.findFragmentByTag(HomeScreen.TAG)
                        val k = childFragmentManager.findFragmentByTag(MyList.TAG)
                        if (f == null) {
                            childFragmentManager.commit {
                                add<Explore>(R.id.mainScreenFrag, Explore.TAG)
                            }
                        } else {
                            childFragmentManager.commit {
                                show(f)
                                if (h != null) {
                                    hide(h)
                                }
                                if (k != null) {
                                    hide(k)
                                }
                            }
                        }
                        true
                    }

                    R.id.action_home -> {
                        val f = childFragmentManager.findFragmentByTag(HomeScreen.TAG)
                        val h = childFragmentManager.findFragmentByTag(Explore.TAG)
                        val k = childFragmentManager.findFragmentByTag(MyList.TAG)
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
                                if (k != null) {
                                    hide(k)
                                }
                            }
                        }
                        true
                    }

                    R.id.action_myList -> {
                        val f = childFragmentManager.findFragmentByTag(MyList.TAG)
                        val h = childFragmentManager.findFragmentByTag(Explore.TAG)
                        val k = childFragmentManager.findFragmentByTag(HomeScreen.TAG)
                        if (f == null) {
                            childFragmentManager.commit {
                                add<MyList>(R.id.mainScreenFrag, MyList.TAG)
                            }
                        } else {
                            childFragmentManager.commit {
                                show(f)
                                if (h != null) {
                                    hide(h)
                                }
                                if (k != null) {
                                    hide(k)
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

    companion object {
        const val TAG = "MainScreen"
    }
}
