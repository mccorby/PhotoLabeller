package com.mccorby.movierating.labeller.presentation

import com.mccorby.movierating.filemanager.FileManager
import com.mccorby.movierating.model.Stats
import com.mccorby.movierating.model.Trainer
import com.mccorby.movierating.trainer.MovieTrainerImpl

class LabellingPresenter<in T>(private val view: LabellingView,
                               private val fileManager: FileManager,
                               private val trainer: Trainer<T>) {

    fun loadModel(): Stats {
        return if (!trainer.isModelLoaded()) {
            val modelFile = fileManager.loadModelFile()
            trainer.loadModel(modelFile)
        } else {
            Stats("Model was already loaded")
        }
    }

    fun predict(data: T): Stats {
        (trainer as MovieTrainerImpl).wordVectorsPath = fileManager.loadFile("NewsWordVector.txt")
        (trainer as MovieTrainerImpl).dataDir = fileManager.rootDir()
        return trainer.predict(data)
    }
}

