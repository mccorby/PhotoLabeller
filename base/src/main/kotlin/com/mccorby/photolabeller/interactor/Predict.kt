package com.mccorby.photolabeller.interactor

import com.mccorby.executors.ExecutionContext
import com.mccorby.photolabeller.model.Stats
import com.mccorby.photolabeller.model.Trainer
import java.io.File

class Predict(private val trainer: Trainer,
              executionContext: ExecutionContext,
              postExecutionContext: ExecutionContext): UseCase<Stats, PredictParams>(executionContext, postExecutionContext) {

    override suspend fun run(params: PredictParams): Stats = trainer.predict(params.file)
}

class PredictParams(val file: File)