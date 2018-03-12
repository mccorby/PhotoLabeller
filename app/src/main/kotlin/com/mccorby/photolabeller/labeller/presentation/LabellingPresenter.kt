package com.mccorby.photolabeller.labeller.presentation

import com.mccorby.movierating.trainer.MovieTrainerImpl
import com.mccorby.photolabeller.filemanager.FileManager
import com.mccorby.photolabeller.model.Stats
import com.mccorby.photolabeller.model.Trainer
import java.io.File

class LabellingPresenter<in T>(private val view: LabellingView, private val fileManager: FileManager,
                         private val trainer: Trainer<T>) {

    fun saveLabelledImage(photoPath: String, label: String) {
        fileManager.saveLabelImage(photoPath, label)
    }

    fun loadModel(): Stats {
        return if (!trainer.isModelLoaded()) {
            val modelFile = fileManager.loadModelFile()
            trainer.loadModel(modelFile)
        } else {
            Stats("Model was already loaded")
        }
    }

    fun createImageFile(): File {
        return fileManager.createImageFile()
    }

    fun predict(image: T): Stats {
        (trainer as MovieTrainerImpl).wordVectorsPath = fileManager.loadFile("NewsWordVector.txt")
        (trainer as MovieTrainerImpl).dataDir = fileManager.rootDir()
        return trainer.predict(image)
    }
}

