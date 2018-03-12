package com.mccorby.photolabeller.trainer.presentation

import com.mccorby.photolabeller.filemanager.FileManager
import com.mccorby.photolabeller.model.Stats
import com.mccorby.photolabeller.model.Trainer

class TrainingPresenter(private val trainer: Trainer<String>, val fileManager: FileManager) {

    fun train(): Stats {
        return trainer.train()
    }
}