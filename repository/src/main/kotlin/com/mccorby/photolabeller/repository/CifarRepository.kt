package com.mccorby.photolabeller.repository

import com.mccorby.fp.Either
import java.io.File

class CifarRepository(private val localDataSource: LocalDataSource,
                      private val dataSource: FederatedDataSource,
                      private val embeddedDataSource: EmbeddedDataSource) : FederatedRepository {
    override fun createImage(): File = localDataSource.createTempFile("png")

    override fun saveLabelImage(photoPath: String, label: String) = localDataSource.saveLabelImage(photoPath, label)

    override fun openModel(): File {
        val modelFile = localDataSource.loadModelFile()
        return when (modelFile) {
            is Either.Right -> modelFile.value
            is Either.Left -> tryNextLevel()
        }
    }

    private fun tryNextLevel(): File {
        println("trying with remote model")
        val modelFile = dataSource.updateModel()
        return when (modelFile) {
            is Either.Right -> modelFile.value
            is Either.Left -> retrieveEmbeddedModel()
        }
    }

    private fun retrieveEmbeddedModel(): File {
        println("Retrieving embedded model")
        return embeddedDataSource.getModel()
    }

    override fun updateLocalModel(): Either<Exception, File> = dataSource.updateModel()

    override fun createModelFile(): File = localDataSource.createTempFile("model")

    override fun sendLocalModel(file: File, samples: Int) = dataSource.uploadModel(file, samples)

    override fun sendModelUpdate(modelUpdate: ByteArray, samples: Int) = dataSource.uploadModelUpdate(modelUpdate, samples)
}
