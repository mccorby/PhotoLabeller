package com.mccorby.photolabeller.model

import java.io.File

interface Trainer {
    fun loadModel(location: File): Stats
    fun train(numSamples: Int, epochs: Int): Stats
    fun predict(file: File): Stats
    fun isModelLoaded(): Boolean
    fun saveModel(file: File): File
    fun getSamplesInTraining(): Int
    fun getUpdateFromLayer(): ByteArray
}