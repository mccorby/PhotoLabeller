package com.mccorby.photolabeller.trainer

import com.mccorby.photolabeller.config.SharedConfig
import com.mccorby.photolabeller.model.Stats

interface TrainingView {
    fun onStartTraining(config: SharedConfig)
    fun showTrainingStats(stats: Stats)
}