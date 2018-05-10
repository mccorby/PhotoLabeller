package com.mccorby.photolabeller.datasource

import com.mccorby.fp.Either
import com.mccorby.photolabeller.datasource.network.FederatedService
import com.mccorby.photolabeller.model.TrainingRound
import com.mccorby.photolabeller.repository.FederatedDataSource
import com.mccorby.photolabeller.repository.LocalDataSource
import kotlinx.coroutines.experimental.runBlocking
import java.io.File

class NetworkDataSource(private val service: FederatedService, private val localDataSource: LocalDataSource) : FederatedDataSource {
    override fun updateModel(): Either<Exception, File> = runBlocking {
        try {
            val stream = service.getModel().await().byteStream()
            Either.Right(localDataSource.serializeModel(stream))
        } catch (ex: Exception) {
            print(ex.message)
            Either.Left(ex)
        }
    }

    override fun getCurrentRound(): TrainingRound {
        return runBlocking {
            try {
                service.getCurrentTrainingRound().await()
            } catch (ex: Exception) {
                TrainingRound("", Long.MIN_VALUE, Long.MIN_VALUE)
            }
        }
    }

    override fun uploadModel(file: File, samples: Int): Boolean {
        return runBlocking {
            try {
                // Note. file.readBytes can handle a max of 2Gb
                service.sendUpdate(file.readBytes(), samples).await()
            } catch (ex: Exception) {
                false
            }
        }
    }

    override fun uploadModelUpdate(modelUpdate: ByteArray, samples: Int): Boolean {
        return runBlocking {
            try {
                // Note. file.readBytes can handle a max of 2Gb
                service.sendUpdate(modelUpdate, samples).await()
            } catch (ex: Exception) {
                false
            }
        }
    }
}