package com.mccorby.movierating.labeller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.mccorby.movierating.R
import com.mccorby.movierating.trainer.TrainingActivityFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        navigation.setOnNavigationItemSelectedListener { item ->
            val selectedFragment = when (item.itemId) {
                R.id.action_browse -> {
                    MainActivityFragment()
                }
                R.id.action_train -> {
                    TrainingActivityFragment()
                }
                else -> MainActivityFragment()
            }
            changeFragment(selectedFragment)
            true
        }

        if (savedInstanceState == null) {
            changeFragment(MainActivityFragment())
        }
    }

    private fun changeFragment(selectedFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, selectedFragment)
        transaction.commit()
    }
}
