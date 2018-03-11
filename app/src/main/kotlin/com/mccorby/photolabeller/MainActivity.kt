package com.mccorby.photolabeller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fragment = supportFragmentManager.findFragmentById(R.id.fragment) as MainActivityFragment


        fab.setOnClickListener { fragment.dispatchTakePictureIntent()}
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Override this method in the activity that hosts the Fragment and call super
        // in order to receive the result inside onActivityResult from the fragment.
        super.onActivityResult(requestCode, resultCode, data)
    }

}
