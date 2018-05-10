package com.mccorby.photolabeller.repository

import com.mccorby.fp.Either
import java.io.File

interface FederatedRepository {
    fun openModel(): File
    fun createImage(): File
    fun saveLabelImage(photoPath: String, label: String): File
    fun updateLocalModel(): Either<Exception, File>
    fun createModelFile(): File
    fun sendLocalModel(file: File, samples: Int): Boolean
    fun sendModelUpdate(modelUpdate: ByteArray, samples: Int): Boolean
}