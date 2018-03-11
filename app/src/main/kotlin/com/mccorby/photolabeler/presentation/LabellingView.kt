package com.mccorby.photolabeler.presentation

import com.mccorby.photolabeler.model.Stats

interface LabellingView {

    fun onModelLoaded(stats: Stats)
}