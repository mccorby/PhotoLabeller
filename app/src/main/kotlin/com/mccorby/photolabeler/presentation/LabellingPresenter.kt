package com.mccorby.photolabeler.presentation

import com.mccorby.photolabeler.filemanager.FileManager
import com.mccorby.photolabeler.model.Stats
import com.mccorby.photolabeler.trainer.Trainer
import java.io.File

class LabellingPresenter(private val view: LabellingView, private val fileManager: FileManager, private val trainer: Trainer) {

    fun saveLabelledImage(photoPath: String, label: String) {
        fileManager.saveLabelImage(photoPath, label)
    }

    fun loadModel(): Stats {
        val modelFile = fileManager.loadModelFile()
        return trainer.loadModel(modelFile)
    }

    fun createImageFile(): File {
        return fileManager.createImageFile()
    }
}

