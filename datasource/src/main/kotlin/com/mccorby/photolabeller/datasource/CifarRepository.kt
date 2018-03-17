package com.mccorby.photolabeller.datasource

import com.mccorby.fp.Either
import com.mccorby.photolabeller.filemanager.LocalDataSource
import com.mccorby.photolabeller.model.TrainingRound
import com.mccorby.photolabeller.repository.FederatedRepository
import java.io.File

interface FederatedDataSource {
    fun updateModel(): Either<Exception, File>
    fun getCurrentRound(): TrainingRound
    fun uploadModel(file: File, samples: Int): Boolean
    fun uploadModelUpdate(modelUpdate: ByteArray, samples: Int): Boolean
}

interface EmbeddedDataSource {
    fun getModel(): File
}

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
