package com.mccorby.photolabeller.interactor

import com.mccorby.executors.ExecutionContext
import com.mccorby.photolabeller.model.Stats
import com.mccorby.photolabeller.model.Trainer
import com.mccorby.photolabeller.repository.FederatedRepository

class GetModel(private val repository: FederatedRepository,
               private val trainer: Trainer,
               executionContext: ExecutionContext,
               postExecutionContext: ExecutionContext):
        UseCase<Stats, NoParams>(executionContext, postExecutionContext) {

    override suspend fun run(params: NoParams): Stats {
        return when (trainer.isModelLoaded()) {
            true -> {
                // TODO Use case should no return a string
                Stats("Model was already loaded")
            }
            false -> {
                val modelFile = repository.openModel()
                trainer.loadModel(modelFile)
            }
        }
    }
}
