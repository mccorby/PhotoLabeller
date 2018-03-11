package com.mccorby.photolabeller.presentation

import com.mccorby.photolabeller.model.Stats

interface LabellingView {

    fun onModelLoaded(stats: Stats)
}