package com.mccorby.photolabeller.interactor

import com.mccorby.executors.ExecutionContext
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

abstract class UseCase<out Type, in Params>(private val executionContext: ExecutionContext,
                                            private val postExecutionContext: ExecutionContext) {

    abstract suspend fun run(params: Params): Type

    fun execute(onExecute: (Type) -> Unit, params: Params) {
        val job = async(executionContext.getContext()) { run(params) }
        launch(postExecutionContext.getContext()) {
            onExecute.invoke(job.await())
        }
    }
}

// NOTE Adding NoParams class to allow using Params as data classes
class NoParams