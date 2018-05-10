package com.mccorby.photolabeller.repository

import com.mccorby.fp.Either
import com.mccorby.photolabeller.model.TrainingRound
import java.io.File

interface FederatedDataSource {
    fun updateModel(): Either<Exception, File>
    fun getCurrentRound(): TrainingRound
    fun uploadModel(file: File, samples: Int): Boolean
    fun uploadModelUpdate(modelUpdate: ByteArray, samples: Int): Boolean
}