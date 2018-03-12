package com.mccorby.photolabeller.trainer.presentation

import com.mccorby.photolabeller.filemanager.FileManager
import com.mccorby.photolabeller.model.Stats
import com.mccorby.photolabeller.trainer.Trainer

class TrainingPresenter(private val trainer: Trainer, val fileManager: FileManager) {

    fun train(): Stats {
        return trainer.train()
    }
}