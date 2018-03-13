package com.mccorby.movierating.trainer.presentation

import com.mccorby.movierating.filemanager.FileManager
import com.mccorby.movierating.model.Stats
import com.mccorby.movierating.model.Trainer

class TrainingPresenter(private val trainer: Trainer<String>, val fileManager: FileManager) {

    fun train(): Stats {
        return trainer.train()
    }
}