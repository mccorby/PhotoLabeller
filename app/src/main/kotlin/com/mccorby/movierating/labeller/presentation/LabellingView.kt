package com.mccorby.movierating.labeller.presentation

import com.mccorby.movierating.model.Stats

interface LabellingView {

    fun onModelLoaded(stats: Stats)
}