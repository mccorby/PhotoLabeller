package com.mccorby.movierating.trainer.presentation

import com.mccorby.movierating.model.Stats
import com.mccorby.movierating.model.Trainer

class TrainingPresenter(private val trainer: Trainer<String>, private val trainListener: Any) {

    fun train(): Stats {
        return trainer.train(trainListener)
    }
}