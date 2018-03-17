package com.mccorby.photolabeller.interactor

import com.mccorby.executors.ExecutionContext
import com.mccorby.photolabeller.model.Stats
import com.mccorby.photolabeller.model.Trainer
import com.mccorby.photolabeller.repository.FederatedRepository

class Train(private val repository: FederatedRepository,
            private val trainer: Trainer,
            executionContext: ExecutionContext,
            postExecutionContext: ExecutionContext): UseCase<Stats,TrainParams>(executionContext, postExecutionContext) {

    override suspend fun run(params: TrainParams): Stats {
        println("Train started")
        val stats = trainer.train(params.maxSamples, params.epochs)
        println("Train finished")
        trainer.saveModel(repository.createModelFile())
        val update = trainer.getUpdateFromLayer()
        repository.sendModelUpdate(update, trainer.getSamplesInTraining())
        return stats
    }
}

data class TrainParams(val maxSamples: Int, val epochs: Int)