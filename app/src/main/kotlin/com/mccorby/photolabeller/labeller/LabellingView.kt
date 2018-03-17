package com.mccorby.photolabeller.labeller

import com.mccorby.photolabeller.model.Stats

interface LabellingView {

    fun onModelLoaded(stats: Stats)
    fun onPrediction(stats: Stats)
}