package com.mccorby.photolabeller.presentation

import com.mccorby.photolabeller.filemanager.FileManager
import com.mccorby.photolabeller.model.Stats
import com.mccorby.photolabeller.trainer.Trainer
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

