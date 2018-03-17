package com.mccorby.photolabeller.interactor

import com.mccorby.executors.ExecutionContext
import com.mccorby.photolabeller.repository.FederatedRepository
import java.io.File

class LabelImage(private val repository: FederatedRepository,
                 executionContext: ExecutionContext,
                 postExecutionContext: ExecutionContext):
        UseCase<File, LabelImageParams>(executionContext, postExecutionContext) {
    
    override suspend fun run(params: LabelImageParams) = repository.saveLabelImage(params.imagePath, params.label)
}

data class LabelImageParams(val imagePath: String, val label: String)