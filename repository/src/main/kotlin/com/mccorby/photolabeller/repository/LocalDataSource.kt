package com.mccorby.photolabeller.repository

import com.mccorby.fp.Either
import java.io.File
import java.io.InputStream

interface LocalDataSource {
    fun createTempFile(type: String): File
    fun saveLabelImage(currentPhotoPath: String, selectedLabel: String): File
    fun loadModelFile(): Either<Exception, File>
    fun loadTrainingFiles(): Map<String, List<File>>
    fun serializeModel(initialStream: InputStream?): File
}