package com.mccorby.photolabeler.presentation

import com.mccorby.photolabeler.filemanager.FileManager
import com.mccorby.photolabeler.model.Stats
import com.mccorby.photolabeler.trainer.Trainer

class TrainingPresenter(private val trainer: Trainer, val fileManager: FileManager) {

    fun train(): Stats {
        return trainer.train()
    }
}