package com.mccorby.photolabeller

import android.app.Application
import android.content.Context
import com.mccorby.photolabeller.di.*
import space.traversal.kapsule.transitive

class AndroidClientApplication : Application() {

    companion object {
        fun appModule(context: Context) = (context.applicationContext as AndroidClientApplication).module
    }

    private lateinit var module: Modules

    override fun onCreate() {
        super.onCreate()
        module = createDIModule()
    }

    private fun createDIModule() = Modules(
            android = MainAppModule(this, MainNetworkModule()),
            labelling = MainLabellingModule(),
            training = MainTrainingModule(),
            network = MainNetworkModule())
            .transitive()
}