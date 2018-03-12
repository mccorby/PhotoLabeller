package com.mccorby.photolabeller.labeller.presentation

import com.mccorby.photolabeller.filemanager.FileManager
import com.mccorby.photolabeller.model.Stats
import com.mccorby.photolabeller.trainer.Trainer
import java.io.File

class LabellingPresenter(private val view: LabellingView, private val fileManager: FileManager, private val trainer: Trainer) {

    fun saveLabelledImage(photoPath: String, label: String) {
        fileManager.saveLabelImage(photoPath, label)
    }

    fun loadModel(): Stats {
        if (!trainer.isModelLoaded()) {
            val modelFile = fileManager.loadModelFile()
            return trainer.loadModel(modelFile)
        } else {
            return Stats("Model was already loaded")
        }
    }

    fun createImageFile(): File {
        return fileManager.createImageFile()
    }

    fun predict(image: File): Stats {
        return trainer.predict(image)
    }
}

