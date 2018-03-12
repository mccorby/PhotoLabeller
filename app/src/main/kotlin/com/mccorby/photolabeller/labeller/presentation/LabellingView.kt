package com.mccorby.photolabeller.labeller.presentation

import com.mccorby.photolabeller.model.Stats

interface LabellingView {

    fun onModelLoaded(stats: Stats)
}