package com.mccorby.movierating.model

import java.io.File

interface Trainer<in T> {
    fun train(): Stats
    fun predict(text: T): Stats
    fun loadModel(location: File): Stats
    fun isModelLoaded(): Boolean
}
